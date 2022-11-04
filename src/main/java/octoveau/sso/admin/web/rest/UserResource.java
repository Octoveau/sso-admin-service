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
import java.util.List;

/**
 * UserResource
 *
 * @author yifanzheng
 */
@RestController
@RequestMapping("/api")
@Api(tags = {"User Resource"})
public class UserResource {

    @Autowired
    private UserService userService;


    @GetMapping("/users")
    @ApiOperation(value = "获取所有用户")
    public ResponseDTO<List<UserDTO>> listUsers() {
        List<UserDTO> userDTOS = userService.listUsers();
        return ResponseDTO.ok(userDTOS);
    }

    @GetMapping("/users/{userPhone}")
    @ApiOperation(value = "根据用户名获取用户信息")
    public ResponseDTO<UserDTO> getUser(@PathVariable String userPhone) {
        UserDTO userDTO = userService.getUserInfoByPhone(userPhone);
        return ResponseDTO.ok(userDTO);
    }

    @PostMapping("/users/register")
    @ApiOperation(value = "用户注册")
    public ResponseDTO<Void> register(@RequestBody @Valid UserRegisterRequest userRegister) {
        userService.register(userRegister);
        return ResponseDTO.ok();
    }

    @DeleteMapping("/users/{userPhone}")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "根据用户名删除用户信息")
    public ResponseDTO<Void> deleteByUserPhone(@PathVariable("userPhone") String userPhone) {
        userService.delete(userPhone);
        return ResponseDTO.ok();
    }

}
