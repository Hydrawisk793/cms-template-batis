package kaphein.template.cmstemplatebatis.web;

import java.util.Collections;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;

/**
 * `application.properties`에 API configuration에 관한 key-value 쌍을 노출한다.
 */
@Configuration
public class ApiConfig
{
    @ConfigurationProperties("api")
    @Bean
    public ApiProperties apiProperties()
    {
        return new ApiProperties();
    }

    @ConfigurationProperties("api.cors")
    @Bean
    public CorsConfiguration apiCorsConfiguration()
    {
        final CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowedMethods(Collections.singletonList("*"));
        cors.setAllowedHeaders(Collections.singletonList("*"));
        cors.setMaxAge(1800L);

        return cors;
    }
}
