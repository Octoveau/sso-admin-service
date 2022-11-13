package octoveau.sso.admin.web.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import octoveau.sso.admin.dto.ResponseDTO;
import octoveau.sso.admin.dto.UserDTO;
import octoveau.sso.admin.entity.PageObject;
import octoveau.sso.admin.service.UserService;
import octoveau.sso.admin.web.rest.request.UserRegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public ResponseDTO<PageObject<UserDTO>> listUsers(@SortDefault(sort = "lastModifiedDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<UserDTO> userPage = userService.queryUsers(pageable);
        PageObject<UserDTO> pageObject = PageObject.of(userPage.getTotalElements(), userPage.getContent());

        return ResponseDTO.ok(pageObject);
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
