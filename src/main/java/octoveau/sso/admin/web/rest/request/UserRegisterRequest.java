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
    private String phone;

    @Size(min = 4, max = 30)
    private String nickName;

    @NotBlank
    @Size(min = 6, max = 68)
    private String password;

    public User toEntity() {
        User user = new User();
        BeanUtils.copyProperties(this, user);

        return user;
    }

}