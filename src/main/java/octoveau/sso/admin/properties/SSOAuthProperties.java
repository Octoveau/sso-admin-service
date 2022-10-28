package octoveau.sso.admin.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * SSOAuthProperties
 *
 * @author yifanzheng
 */
@Getter
@Configuration
@ConfigurationProperties(prefix = "sso.auth")
public class SSOAuthProperties {

    private Long siteTicketTtl;

    private Long siteTokenTtl;

    private Long siteRefreshTokenTtl;
}
