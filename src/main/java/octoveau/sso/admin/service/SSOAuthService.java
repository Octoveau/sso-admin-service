package octoveau.sso.admin.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import octoveau.sso.admin.constant.CommonConstants;
import octoveau.sso.admin.constant.UserRoleConstants;
import octoveau.sso.admin.dto.*;
import octoveau.sso.admin.entity.User;
import octoveau.sso.admin.exception.NotFoundException;
import octoveau.sso.admin.exception.UnauthorizedAccessException;
import octoveau.sso.admin.properties.SSOAuthProperties;
import octoveau.sso.admin.security.JwtUtils;
import octoveau.sso.admin.cache.SiteTicketCache;
import octoveau.sso.admin.util.IDGeneratorUtil;
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

    /**
     * 用户登录认证
     *
     * @param userLogin 用户登录信息
     */
    public JwtUserDTO authLogin(UserLoginDTO userLogin) {
        String userName = userLogin.getUserName();
        String password = userLogin.getPassword();

        // 根据登录名获取用户信息
        Optional<User> userOptional = userService.getUserByName(userName);
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
        UserDTO userDTO = new UserDTO();
        userDTO.setUserName(userName);
        userDTO.setEmail(user.getEmail());
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

    public SSOSiteTicketDTO getTicketAndCache(String siteKey) {
        SiteTicketCache siteTicketCache = this.generateSiteTicketCache(siteKey);
        String ticket = IDGeneratorUtil.generateUUID();
        // 将ticket关联的cache进行缓存
        ssoSiteTicketStorage.cacheSiteTicket(siteKey, siteTicketCache);

        SSOSiteTicketDTO siteTicketDTO = new SSOSiteTicketDTO();
        siteTicketDTO.setTicket(ticket);
        siteTicketDTO.setCallbackUrl(siteTicketCache.getCallbackUrl());

        return siteTicketDTO;
    }

    public SSOSiteTokenDTO getSiteToken(SSOTokenRequest tokenRequest) {
        SiteTicketCache siteTicketCache = ssoSiteTicketStorage.getCache(tokenRequest.getTicket());
        if (Objects.isNull(siteTicketCache)) {
            throw new UnauthorizedAccessException("Error ticket");
        }
        if (!StringUtils.equals(siteTicketCache.getSiteKey(), tokenRequest.getSiteKey())
                || !StringUtils.equals(siteTicketCache.getSiteSecret(), tokenRequest.getSiteSecret())) {
            throw new UnauthorizedAccessException("Invalid SiteKey or SiteSecret");
        }
        long tokenTTL = normalizeLong(ssoAuthProperties.getSiteTokenTtl(), CommonConstants.DEFAULT_TOKEN_TTL);
        long refreshTokenTTL = normalizeLong(ssoAuthProperties.getSiteRefreshTokenTtl(), CommonConstants.DEFAULT_REFRESH_TOKEN_TTL);
        Instant now = Instant.now();

        SSOSiteTokenDTO ssoTokenInfoDTO = new SSOSiteTokenDTO();
        ssoTokenInfoDTO.setToken(siteTicketCache.getToken());
        ssoTokenInfoDTO.setExpireSeconds(tokenTTL);
        ssoTokenInfoDTO.setExpires(now.plusSeconds(tokenTTL));
        ssoTokenInfoDTO.setRefreshToken(siteTicketCache.getRefreshToken());
        ssoTokenInfoDTO.setRefreshTokenExpireSeconds(refreshTokenTTL);
        ssoTokenInfoDTO.setRefreshTokenExpires(now.plusSeconds(refreshTokenTTL));

        return ssoTokenInfoDTO;
    }

    public SSOSiteTokenDTO refreshToken() {
        return null;
    }

    public void getUserByToken(String token) {
     
    }

    private SiteTicketCache generateSiteTicketCache(String siteKey) {
        Optional<SiteDTO> siteOptional = siteService.getSite(siteKey);
        if (!siteOptional.isPresent()) {
            throw new NotFoundException("Site not exists");
        }
        SiteDTO siteDTO = siteOptional.get();
        SiteTicketCache siteTicketCache = new SiteTicketCache();
        siteTicketCache.setSiteName(siteDTO.getSiteName());
        siteTicketCache.setSiteKey(siteDTO.getSiteKey());
        siteTicketCache.setSiteSecret(siteDTO.getSiteSecret());
        siteTicketCache.setCallbackUrl(siteDTO.getCallbackUrl());
        siteTicketCache.setToken(String.format("%s%s", IDGeneratorUtil.generateUUID(), IDGeneratorUtil.generateUUID()));
        siteTicketCache.setRefreshToken(IDGeneratorUtil.generateUUID());

        return siteTicketCache;
    }

    private Long normalizeLong(Long value, Long defaultValue) {
        if (value == null || value <= 0) {
            return defaultValue;
        }
        return value;
    }

    private class SSOSiteTicketStorage {

        private final Cache<String, SiteTicketCache> ticketCache;

        private SSOSiteTicketStorage() {
            long ticketTTL = normalizeLong(ssoAuthProperties.getSiteTicketTtl(), CommonConstants.DEFAULT_TICKET_TTL);
            ticketCache = CacheBuilder.newBuilder()
                    .expireAfterWrite(ticketTTL, TimeUnit.SECONDS)
                    .build();
        }

        void cacheSiteTicket(String ticket, SiteTicketCache siteTicketCache) {
            ticketCache.put(ticket, siteTicketCache);
        }

        SiteTicketCache getCache(String ticket) {
            return ticketCache.getIfPresent(ticket);
        }
    }


    private class SSOSiteTokenStorage {

        private final Cache<String, SiteTicketCache> ticketCache;

        private SSOSiteTokenStorage() {
            long tokenTTL = normalizeLong(ssoAuthProperties.getSiteTokenTtl(), CommonConstants.DEFAULT_TOKEN_TTL);
            ticketCache = CacheBuilder.newBuilder()
                    .expireAfterWrite(tokenTTL, TimeUnit.SECONDS)
                    .build();
        }

        void cacheSiteToken(String ticket, SiteTicketCache siteTicketCache) {
            ticketCache.put(ticket, siteTicketCache);
        }

        SiteTicketCache getCache(String ticket) {
            return ticketCache.getIfPresent(ticket);
        }
    }

}
