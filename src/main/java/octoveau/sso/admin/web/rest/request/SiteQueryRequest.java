package octoveau.sso.admin.web.rest.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * SiteQueryRequest
 *
 * @author yifanzheng
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SiteQueryRequest {
    private String siteName;
}
