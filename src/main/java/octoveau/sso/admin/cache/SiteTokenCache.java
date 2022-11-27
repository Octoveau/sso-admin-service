package octoveau.sso.admin.cache;

import lombok.Data;

import java.time.Instant;

/**
 * SiteTokenCache
 *
 * @author yifanzheng
 */
@Data
public class SiteTokenCache {

    private String currentPhone;

    private String token;

    /**
     * token的过期时间
     */
    private Instant expires;

    private String refreshToken;

    /**
     * refreshToken的过期时间
     */
    private Instant refreshTokenExpires;
}
