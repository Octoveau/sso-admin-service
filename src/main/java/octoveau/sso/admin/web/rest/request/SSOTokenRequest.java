package octoveau.sso.admin.web.rest.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * SSOTokenRequest
 *
 * @author yifanzheng
 */
@Data
public class SSOTokenRequest {

    @NotBlank
    private String ticket;

    @NotBlank
    private String siteKey;

    @NotBlank
    private String siteSecret;
}
