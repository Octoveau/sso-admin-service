package octoveau.sso.admin.dto;

import lombok.Data;

/**
 * SiteAuthInfoDTO
 *
 * @author yifanzheng
 */
@Data
public class SSOSiteTicketDTO {

    private String ticket;

    private String callbackUrl;
}
