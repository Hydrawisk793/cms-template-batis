package kaphein.template.cmstemplatebatis.web;

import java.util.Collections;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
public class ProtocolConfig
{
    @ConfigurationProperties("protocol")
    @Bean
    public ProtocolProperties protocolProperties()
    {
        return new ProtocolProperties();
    }

    @ConfigurationProperties("protocol.cors")
    @Bean
    public CorsConfiguration protocolCorsConfiguration()
    {
        final CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowedMethods(Collections.singletonList("*"));
        cors.setAllowedHeaders(Collections.singletonList("*"));
        cors.setMaxAge(1800L);

        return cors;
    }
}
