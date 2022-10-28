package octoveau.sso.admin.web.rest.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * SSOTicketRequest
 *
 * @author yifanzheng
 */
@Data
public class SSOTicketRequest {

    @NotBlank
    private String siteKey;
}
