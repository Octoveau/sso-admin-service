package octoveau.sso.admin.web.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import octoveau.sso.admin.dto.ResponseDTO;
import octoveau.sso.admin.dto.UserDTO;
import octoveau.sso.admin.web.rest.request.UserRegisterRequest;
import octoveau.sso.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * UserResource
 *
 * @author yifanzheng
 */
@RestController
@RequestMapping("/api/users")
@Api(tags = {"User Resource"})
public class UserResource {

    @Autowired
    private UserService userService;

    @GetMapping("/{userName}")
    @ApiOperation(value = "根据用户名获取用户信息")
    public ResponseDTO<UserDTO> getUser(@PathVariable String userName) {
        UserDTO userDTO = userService.getUserInfoByPhone(userName);
        return ResponseDTO.ok(userDTO);
    }

    @PostMapping("/register")
    @ApiOperation(value = "用户注册")
    public ResponseDTO<Void> register(@RequestBody @Valid UserRegisterRequest userRegister) {
        userService.register(userRegister);
        return ResponseDTO.ok();
    }

    @DeleteMapping("/{userName}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "根据用户名删除用户信息")
    public ResponseDTO<Void> deleteByUserName(@PathVariable("userName") String userName) {
        userService.delete(userName);
        return ResponseDTO.ok();
    }

}
