package octoveau.sso.admin.web.rest.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * SiteRequest
 *
 * @author yifanzheng
 */
@Data
public class SiteRequest {

    @NotBlank
    private String siteName;

    @NotBlank
    private String callbackUrl;
}
