package octoveau.sso.admin.web.rest.request;

import lombok.Data;
import octoveau.sso.admin.entity.User;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * UserRegisterDTO
 *
 * @author yifanzheng
 **/
@Data
public class UserRegisterRequest {

    @NotBlank
    @Size(min = 4, max = 30)
    private String userName;

    @NotBlank
    @Size(min = 4, max = 30)
    private String nickName;

    @NotBlank
    @Size(min = 6, max = 15)
    private String password;

    @NotBlank
    @Email(message = "邮箱格式不对")
    @Size(max = 40)
    private String email;

    public User toEntity() {
        User user = new User();
        BeanUtils.copyProperties(this, user);

        return user;
    }

}