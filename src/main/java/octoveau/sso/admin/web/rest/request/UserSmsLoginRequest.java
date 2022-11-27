package octoveau.sso.admin.web.rest.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * UserSmsLoginDTO
 *
 * @author sz7v
 */
@Data
public class UserSmsLoginRequest {

    @NotBlank
    private String phone;

    @NotBlank
    private String smsCode;
}
