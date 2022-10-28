package octoveau.sso.admin.dto;

import lombok.Data;

import java.time.Instant;

/**
 * SSOTokenInfoDTO
 *
 * @author yifanzheng
 */
@Data
public class SSOSiteTokenDTO {

    private String token;

    /**
     * token的有效时间
     */
    private Long expireSeconds;

    /**
     * token的过期时间
     */
    private Instant expires;

    private String refreshToken;

    /**
     * refreshToken的有效时间
     */
    private Long refreshTokenExpireSeconds;

    /**
     * refreshToken的过期时间
     */
    private Instant refreshTokenExpires;

}
