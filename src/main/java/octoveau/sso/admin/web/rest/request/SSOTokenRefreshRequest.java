package octoveau.sso.admin.web.rest.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * SSOTokenRefreshRequest
 *
 * @author yifanzheng
 */
@Data
public class SSOTokenRefreshRequest {

    @NotBlank
    private String token;

    @NotBlank
    private String refreshToken;
}
