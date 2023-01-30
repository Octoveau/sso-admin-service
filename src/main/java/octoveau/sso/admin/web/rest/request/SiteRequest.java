package octoveau.sso.admin.web.rest.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

/**
 * SiteRequest
 *
 * @author yifanzheng
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SiteRequest {

    @NotBlank
    private String siteName;

    @NotBlank
    private String callbackUrl;

    @Max(value = 120, message = "Remark too long")
    private String remark;
}
