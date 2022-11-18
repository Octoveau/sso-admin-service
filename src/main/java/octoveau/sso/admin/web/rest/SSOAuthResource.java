package octoveau.sso.admin.web.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import octoveau.sso.admin.dto.*;
import octoveau.sso.admin.security.SecurityUtils;
import octoveau.sso.admin.service.SSOAuthService;
import octoveau.sso.admin.web.rest.request.SSOTicketRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public ResponseDTO<JwtUserDTO> login(@RequestBody UserLoginDTO userLogin) {
        // 用户登录认证
        JwtUserDTO jwtUser = ssoAuthService.authLogin(userLogin);
        return ResponseDTO.ok(jwtUser);
    }

    @PostMapping("/sms/login")
    @ApiOperation(value = "验证码登录")
    public ResponseDTO<JwtUserDTO> smsLogin(@RequestBody UserSmsLoginDTO userSmsLogin) {
        JwtUserDTO jwtUserDTO = ssoAuthService.authSmsLogin(userSmsLogin);
        return ResponseDTO.ok(jwtUserDTO);
    }

    @PostMapping("/sms")
    @ApiOperation(value = "发送短信获取验证码")
    public ResponseDTO<Void> sendSMS(@RequestParam("phone") String phone) {
        ssoAuthService.sendShortMessage(phone);
        return ResponseDTO.ok();
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
