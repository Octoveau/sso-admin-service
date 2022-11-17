package octoveau.sso.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.time.Instant;
import java.util.TimeZone;

@EnableTransactionManagement
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
@SpringBootApplication
public class SSOAdminApplication {
    
    private static final Logger log = LoggerFactory.getLogger(SSOAdminApplication.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SSOAdminApplication.class);
        Environment env = app.run(args).getEnvironment();
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            log.warn("The host name could not be determined, using `localhost` as fallback.");
        }

        log.info("\n----------------------------------------------------------\n\t"
                    + "Application '{}' is running! Access URLs:\n\t" 
                    + "Local: \t\t{}://localhost:{}\n\t"
                    + "External: \t{}://{}:{}\n\t"
                    + "Profile(s): \t{}"
                    + "\n----------------------------------------------------------",
                env.getProperty("spring.application.name"), 
                protocol, 
                env.getProperty("server.port"), 
                protocol,
                hostAddress, 
                env.getProperty("server.port"), 
                env.getActiveProfiles());
    }

    @PostConstruct
    void started() {
        // 将应用程序置身于UTC时区之中，以统一不同时区的时间
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        log.info("Change time zone to UTC, current time: {}", Instant.now());
    }

    @Bean
    @Primary
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        RestTemplate rest = builder.build();
        rest.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        return rest;
    }
}
