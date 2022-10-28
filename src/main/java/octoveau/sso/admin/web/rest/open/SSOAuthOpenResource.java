package octoveau.sso.admin.web.rest.open;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import octoveau.sso.admin.dto.ResponseDTO;
import octoveau.sso.admin.dto.SSOSiteTokenDTO;
import octoveau.sso.admin.dto.UserDTO;
import octoveau.sso.admin.service.SSOAuthService;
import octoveau.sso.admin.web.rest.request.SSOTokenRefreshRequest;
import octoveau.sso.admin.web.rest.request.SSOTokenRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * SSOAccountOpenResource
 * <p>
 * 用户登录成功后，会跳转到本站指定的callback url，ticket参数通过query param传递。
 * 如果登录成功后需要传递其他参数，请在跳转到SSO地址时添加自己的参数（如：http://sso.com/{siteKey}?key1=value1&key2=value2）。
 * 用户登录成功后，会将这些参数通过callback url传递。例如：https://callbackurl?ticket=****&key1=value1&key2=value2。
 *
 * @author yifanzheng
 */
@RestController
@RequestMapping("/openapi/auth")
@Api(tags = "SSO Account Open Resource")
public class SSOAuthOpenResource {

    @Autowired
    private SSOAuthService ssoAuthService;

    @PostMapping("/token")
    @ApiOperation(value = "获取token")
    public ResponseDTO<SSOSiteTokenDTO> getSSOToken(@RequestBody SSOTokenRequest tokenRequest) {
        SSOSiteTokenDTO siteToken = ssoAuthService.getSiteToken(tokenRequest);
        return ResponseDTO.ok(siteToken);
    }

    @PutMapping("/token/refresh")
    @ApiOperation(value = "刷新token")
    public ResponseDTO<SSOSiteTokenDTO> refreshSSOToken(@RequestBody SSOTokenRefreshRequest tokenRefreshRequest) {
        return ResponseDTO.ok(null);
    }

    @GetMapping("/user")
    @ApiOperation(value = "根据token获取用户信息")
    public ResponseDTO<UserDTO> getUserInfo(@RequestParam("token") String token) {
        return ResponseDTO.ok(null);
    }

    @PostMapping("/sites/{siteKey}/logout")
    @ApiOperation(value = "登出指定站点")
    public ResponseEntity<Void> logoutSite(@PathVariable("siteKey") String siteKey) {
        return ResponseEntity.ok().build();
    }

}
