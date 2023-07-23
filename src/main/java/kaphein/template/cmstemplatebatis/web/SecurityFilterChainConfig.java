package kaphein.template.cmstemplatebatis.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.annotation.Resource;

@Order
@Configuration
public class SecurityFilterChainConfig
{
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher()
    {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public SecurityFilterChain filterChain() throws Exception
    {
        httpSecurity
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .maximumSessions(-1);

        httpSecurity
            .cors()
            .configurationSource(urlBasedCorsConfigurationSource);

        httpSecurity
            .csrf()
            .disable();

        return httpSecurity.build();
    }

    @Resource(name = "defaultHttpSecurity")
    public void setHttpSecurity(HttpSecurity httpSecurity)
    {
        this.httpSecurity = httpSecurity;
    }

    @Resource(type = UrlBasedCorsConfigurationSource.class)
    public void setUrlBasedCorsConfigurationSource(UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource)
    {
        this.urlBasedCorsConfigurationSource = urlBasedCorsConfigurationSource;
    }

    private UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource;

    private HttpSecurity httpSecurity;
}
