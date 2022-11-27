package octoveau.sso.admin.web.rest.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * UserPasswordResetRequest
 *
 * @author yifanzheng
 */
@Data
public class UserPasswordUpdateRequest {

    @NotBlank
    private String phone;

    @NotBlank
    private String smsCode;

    @NotBlank
    private String password;
}
