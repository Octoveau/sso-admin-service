package octoveau.sso.admin.web.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import octoveau.sso.admin.dto.*;
import octoveau.sso.admin.service.SSOAuthService;
import octoveau.sso.admin.web.rest.request.SSOTicketRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/current/loginUser")
    @ApiOperation(value = "获取当前登录用户")
    public ResponseDTO<UserDTO> getCurrentLoginUser() {
        return ResponseDTO.ok(null);
    }

    @PostMapping("/ticket")
    @ApiOperation(value = "获取站点的ticket")
    public ResponseDTO<SSOSiteTicketDTO> getSSOTicketBySiteKey(@RequestBody SSOTicketRequest ticketRequest) {
        SSOSiteTicketDTO ticketAndCache = ssoAuthService.getTicketAndCache(ticketRequest.getSiteKey());
        return ResponseDTO.ok(ticketAndCache);
    }

    @PostMapping("/logout")
    @ApiOperation(value = "用户退出登录")
    public ResponseDTO<Void> logout() {
        ssoAuthService.logout();
        return ResponseDTO.ok();
    }
}
