package octoveau.sso.admin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import octoveau.sso.admin.security.SecurityUtils;

import java.util.Optional;

/**
 * 监听 @CreateBy @LastModifiedBy 自动注入用户名
 *
 * @author yifanzheng
 **/
@Configuration
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Optional<String> userLogin = SecurityUtils.getCurrentUserLogin();
        if (userLogin.isPresent()) {
            return userLogin;
        }
        return Optional.of("system");
    }
}
