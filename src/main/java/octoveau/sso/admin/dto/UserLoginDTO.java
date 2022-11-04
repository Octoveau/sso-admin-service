package octoveau.sso.admin.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * UserLoginDTO
 *
 * @author yifanzheng
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserLoginDTO {

    private String userName;

    private String password;

    /**
     * 是否记住我，默认 false
     */
    //private Boolean rememberMe = false;

}
