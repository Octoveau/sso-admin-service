package octoveau.sso.admin.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * UserSmsLoginDTO
 *
 * @author sz7v
 */
@Data
public class UserSmsLoginDTO {

    @NotBlank
    private String phone;

    @NotBlank
    private String smsCode;
}
