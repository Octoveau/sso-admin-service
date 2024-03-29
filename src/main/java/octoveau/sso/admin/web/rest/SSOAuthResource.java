package octoveau.sso.admin.web.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import octoveau.sso.admin.dto.JwtUserDTO;
import octoveau.sso.admin.dto.ResponseDTO;
import octoveau.sso.admin.dto.SSOSiteTicketDTO;
import octoveau.sso.admin.security.SecurityUtils;
import octoveau.sso.admin.service.SSOAuthService;
import octoveau.sso.admin.web.rest.request.SSOTicketRequest;
import octoveau.sso.admin.web.rest.request.UserLoginRequest;
import octoveau.sso.admin.web.rest.request.UserSmsLoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AuthResource
 *
 * @author yifanzheng
 */
@RestController
@RequestMapping("/api/auth")
@Api(tags = "SSO Auth Resource")
public class SSOAuthResource {

    @Autowired
    private SSOAuthService ssoAuthService;

    @PostMapping("/login")
    @ApiOperation(value = "用户登录认证")
    public ResponseDTO<JwtUserDTO> login(@RequestBody UserLoginRequest userLogin) {
        // 用户登录认证
        JwtUserDTO jwtUser = ssoAuthService.authLogin(userLogin);
        return ResponseDTO.ok(jwtUser);
    }

    @PostMapping("/sms/login")
    @ApiOperation(value = "验证码登录")
    public ResponseDTO<JwtUserDTO> smsLogin(@RequestBody UserSmsLoginRequest userSmsLogin) {
        JwtUserDTO jwtUserDTO = ssoAuthService.authSmsLogin(userSmsLogin);
        return ResponseDTO.ok(jwtUserDTO);
    }

    @PostMapping("/ticket")
    @ApiOperation(value = "获取站点的ticket")
    public ResponseDTO<SSOSiteTicketDTO> getSSOTicketBySiteKey(@RequestBody SSOTicketRequest ticketRequest) {
        String userLogin = SecurityUtils.getCurrentUserLogin().orElse("system");
        SSOSiteTicketDTO ticketAndCache =
                ssoAuthService.getTicketAndCacheSite(ticketRequest.getSiteKey(), userLogin);
        return ResponseDTO.ok(ticketAndCache);
    }

    @PostMapping("/logout")
    @ApiOperation(value = "用户退出登录")
    public ResponseDTO<Void> logout() {
        ssoAuthService.logout();
        return ResponseDTO.ok();
    }
}
