package octoveau.sso.admin.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import octoveau.sso.admin.SSOAdminContextHelper;
import octoveau.sso.admin.cache.SiteCache;
import octoveau.sso.admin.cache.SiteTokenCache;
import octoveau.sso.admin.constant.CommonConstants;
import octoveau.sso.admin.constant.UserRoleConstants;
import octoveau.sso.admin.dto.*;
import octoveau.sso.admin.entity.User;
import octoveau.sso.admin.exception.NotFoundException;
import octoveau.sso.admin.exception.UnauthorizedAccessException;
import octoveau.sso.admin.properties.SSOAuthProperties;
import octoveau.sso.admin.security.JwtUtils;
import octoveau.sso.admin.util.IDGeneratorUtil;
import octoveau.sso.admin.web.rest.request.SSOTokenRefreshRequest;
import octoveau.sso.admin.web.rest.request.SSOTokenRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 用户认证服务
 *
 * @author yifanzheng
 */
@Service
public class SSOAuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private SiteService siteService;

    @Autowired
    private SSOAuthProperties ssoAuthProperties;

    private final SSOSiteTicketStorage ssoSiteTicketStorage = new SSOSiteTicketStorage();
    private final SSOSiteTokenStorage ssoSiteTokenStorage = new SSOSiteTokenStorage();

    /**
     * 用户登录认证
     *
     * @param userLogin 用户登录信息
     */
    public JwtUserDTO authLogin(UserLoginDTO userLogin) {
        String userName = userLogin.getUserName();
        String password = userLogin.getPassword();

        // 根据登录名获取用户信息
        Optional<User> userOptional = userService.getUserByPhone(userName);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException("User not found with userName: " + userName);
        }
        User user = userOptional.get();
        // 验证登录密码是否正确。如果正确，则赋予用户相应权限并生成用户认证信息
        if (!StringUtils.equals(password, user.getPassword())) {
            throw new BadCredentialsException("The user name or password error.");
        }
        List<String> roles = userService.listUserRoles(userName);
        // 如果用户角色为空，则默认赋予 ROLE_USER 角色
        if (CollectionUtils.isEmpty(roles)) {
            roles = Collections.singletonList(UserRoleConstants.ROLE_USER);
        }
        // 生成 token
        String token = JwtUtils.generateToken(userName, roles, userLogin.getRememberMe());

        // 认证成功后，设置认证信息到 Spring Security 上下文中
        Authentication authentication = JwtUtils.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 用户信息
        UserDTO userDTO = user.toDTO();
        userDTO.setRoles(roles);

        return new JwtUserDTO(token, userDTO);
    }

    /**
     * 用户退出登录
     * <p>
     * 清除 Spring Security 上下文中的认证信息
     */
    public void logout() {
        SecurityContextHolder.clearContext();
    }

    public void logoutBySiteToken(String token) {
        ssoSiteTokenStorage.remove(token);
    }

    public SSOSiteTicketDTO getTicketAndCacheSite(String siteKey, String currentUser) {
        String ticket = IDGeneratorUtil.generateUUID();

        SiteCache siteCache = this.generateSiteCache(siteKey, currentUser);
        // 将ticket关联的cache进行缓存
        ssoSiteTicketStorage.cacheSite(ticket, siteCache);

        SSOSiteTicketDTO siteTicketDTO = new SSOSiteTicketDTO();
        siteTicketDTO.setTicket(ticket);
        siteTicketDTO.setCallbackUrl(siteCache.getCallbackUrl());

        return siteTicketDTO;
    }

    public SSOSiteTokenDTO getSiteTokenAndCache(SSOTokenRequest tokenRequest) {
        if (StringUtils.isEmpty(tokenRequest.getTicket())) {
            throw new UnauthorizedAccessException("Error ticket");
        }
        SiteCache siteCache = ssoSiteTicketStorage.getCache(tokenRequest.getTicket());
        if (Objects.isNull(siteCache)) {
            throw new UnauthorizedAccessException("Error ticket or expired");
        }
        if (!StringUtils.equals(siteCache.getSiteKey(), tokenRequest.getSiteKey())
                || !StringUtils.equals(siteCache.getSiteSecret(), tokenRequest.getSiteSecret())) {
            throw new UnauthorizedAccessException("Invalid SiteKey or SiteSecret");
        }
        // 构建SiteToken
        SSOSiteTokenDTO ssoSiteTokenDTO = generateSiteToken(siteCache.getToken(), siteCache.getRefreshToken());
        // 缓存siteToken
        SiteTokenCache siteTokenCache = generateSiteTokenCache(ssoSiteTokenDTO, siteCache.getCurrentUserId());
        ssoSiteTokenStorage.cacheSiteToken(siteCache.getToken(), siteTokenCache);

        return ssoSiteTokenDTO;
    }

    public SSOSiteTokenDTO refreshToken(SSOTokenRefreshRequest tokenRefreshRequest) {
        String token = tokenRefreshRequest.getToken();
        SiteTokenCache tokenCache = ssoSiteTokenStorage.getCache(token);
        if (Objects.isNull(tokenCache)
                || !StringUtils.equals(tokenCache.getRefreshToken(), tokenRefreshRequest.getRefreshToken())) {
            throw new UnauthorizedAccessException("Invalid refresh token or expired");
        }
        // 构建SiteToken
        SSOSiteTokenDTO ssoSiteTokenDTO = generateSiteToken(tokenCache.getToken(), tokenCache.getRefreshToken());
        // 缓存siteToken
        SiteTokenCache siteTokenCache = generateSiteTokenCache(ssoSiteTokenDTO, tokenCache.getCurrentUserName());
        ssoSiteTokenStorage.cacheSiteToken(tokenCache.getToken(), siteTokenCache);

        return ssoSiteTokenDTO;
    }

    public UserDTO getUserByToken(String token) {
        if (StringUtils.isEmpty(token)) {
            throw new UnauthorizedAccessException("Error token");
        }
        SiteTokenCache siteTokenCache = ssoSiteTokenStorage.getCache(token);
        if (Objects.isNull(siteTokenCache) || Instant.now().compareTo(siteTokenCache.getExpires()) > 0) {
            throw new UnauthorizedAccessException("Error token or expired");
        }
        return userService.getUserInfoByPhone(siteTokenCache.getCurrentUserName());
    }

    private SiteCache generateSiteCache(String siteKey, String currentUser) {
        Optional<SiteDTO> siteOptional = siteService.getSite(siteKey);
        if (!siteOptional.isPresent()) {
            throw new NotFoundException("Site not exists");
        }
        SiteDTO siteDTO = siteOptional.get();
        SiteCache siteCache = new SiteCache();
        siteCache.setCurrentUserId(currentUser);
        siteCache.setSiteName(siteDTO.getSiteName());
        siteCache.setSiteKey(siteDTO.getSiteKey());
        siteCache.setSiteSecret(siteDTO.getSiteSecret());
        siteCache.setCallbackUrl(siteDTO.getCallbackUrl());
        siteCache.setToken(String.format("%s%s", IDGeneratorUtil.generateUUID(), IDGeneratorUtil.generateUUID()));
        siteCache.setRefreshToken(IDGeneratorUtil.generateUUID());

        return siteCache;
    }

    private SSOSiteTokenDTO generateSiteToken(String token, String refreshToken) {
        long tokenTTL = normalizeLong(ssoAuthProperties.getSiteTokenTtl(), CommonConstants.DEFAULT_TOKEN_TTL);
        long refreshTokenTTL = normalizeLong(ssoAuthProperties.getSiteRefreshTokenTtl(), CommonConstants.DEFAULT_REFRESH_TOKEN_TTL);
        Instant now = Instant.now();
        // 构建SiteToken
        SSOSiteTokenDTO ssoSiteToken = new SSOSiteTokenDTO();
        ssoSiteToken.setToken(token);
        ssoSiteToken.setExpireSeconds(tokenTTL);
        ssoSiteToken.setExpires(now.plusSeconds(tokenTTL));
        ssoSiteToken.setRefreshToken(refreshToken);
        ssoSiteToken.setRefreshTokenExpireSeconds(refreshTokenTTL);
        ssoSiteToken.setRefreshTokenExpires(now.plusSeconds(refreshTokenTTL));

        return ssoSiteToken;
    }

    private SiteTokenCache generateSiteTokenCache(SSOSiteTokenDTO ssoSiteToken, String currentUserName) {
        SiteTokenCache siteTokenCache = new SiteTokenCache();
        siteTokenCache.setCurrentUserName(currentUserName);
        siteTokenCache.setToken(ssoSiteToken.getToken());
        siteTokenCache.setExpires(ssoSiteToken.getExpires());
        siteTokenCache.setRefreshToken(ssoSiteToken.getRefreshToken());
        siteTokenCache.setRefreshTokenExpires(ssoSiteToken.getRefreshTokenExpires());

        return siteTokenCache;
    }

    private Long normalizeLong(Long value, Long defaultValue) {
        if (value == null || value <= 0) {
            return defaultValue;
        }
        return value;
    }

    private class SSOSiteTicketStorage {

        /**
         * Cache<#ticket, SiteCache>
         */
        private final Cache<String, SiteCache> ticketCache;

        private SSOSiteTicketStorage() {
            SSOAuthProperties ssoAuthProperties = SSOAdminContextHelper.getBean(SSOAuthProperties.class);
            long ticketTTL = normalizeLong(ssoAuthProperties.getSiteTicketTtl(), CommonConstants.DEFAULT_TICKET_TTL);
            ticketCache = CacheBuilder.newBuilder()
                    .maximumSize(100)
                    .expireAfterWrite(ticketTTL, TimeUnit.SECONDS)
                    .build();
        }

        void cacheSite(String ticket, SiteCache siteCache) {
            ticketCache.put(ticket, siteCache);
        }

        SiteCache getCache(String ticket) {
            return ticketCache.getIfPresent(ticket);
        }
    }

    private class SSOSiteTokenStorage {

        /**
         * Cache<#token, SiteTokenCache>
         */
        private final Cache<String, SiteTokenCache> tokenCache;

        private SSOSiteTokenStorage() {
            SSOAuthProperties ssoAuthProperties = SSOAdminContextHelper.getBean(SSOAuthProperties.class);
            long tokenTTL = normalizeLong(ssoAuthProperties.getSiteTokenTtl(), CommonConstants.DEFAULT_TOKEN_TTL);
            long refreshTokenTTL = normalizeLong(ssoAuthProperties.getSiteRefreshTokenTtl(), CommonConstants.DEFAULT_REFRESH_TOKEN_TTL);
            tokenCache = CacheBuilder.newBuilder()
                    .maximumSize(100)
                    .expireAfterWrite(Math.max(tokenTTL, refreshTokenTTL), TimeUnit.SECONDS)
                    .build();
        }

        void cacheSiteToken(String token, SiteTokenCache siteTokenCache) {
            tokenCache.put(token, siteTokenCache);
        }

        SiteTokenCache getCache(String token) {
            return tokenCache.getIfPresent(token);
        }

        void remove(String token) {
            tokenCache.invalidate(token);
        }
    }

}