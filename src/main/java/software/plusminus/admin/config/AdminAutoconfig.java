package software.plusminus.admin.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan("software.plusminus.admin")
public class AdminAutoconfig {

    @Bean
    @ConditionalOnMissingBean(RestOperations.class)
    public RestOperations restOperations() {
        return new RestTemplate();
    }
}
