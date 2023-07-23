package kaphein.template.cmstemplatebatis.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfigurationSourceConfig
{
    @Bean
    public UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource()
    {
        return new UrlBasedCorsConfigurationSource();
    }
}
