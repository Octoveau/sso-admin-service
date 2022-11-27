package octoveau.sso.admin.web.rest.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * UserLoginDTO
 *
 * @author yifanzheng
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserLoginRequest {

    @NotBlank
    private String phone;

    @NotBlank
    private String password;

    /**
     * 是否记住我，默认 false
     */
    //private Boolean rememberMe = false;

}
