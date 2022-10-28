package octoveau.sso.admin.cache;

import lombok.Data;

/**
 * SiteTicketCache
 *
 * @author yifanzheng
 */
@Data
public class SiteTicketCache {

    private String siteName;
    private String siteKey;
    private String siteSecret;
    private String callbackUrl;
    private String token;
    private String refreshToken;
}
