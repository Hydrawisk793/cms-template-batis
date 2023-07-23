package kaphein.template.cmstemplatebatis.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class HttpSecurityConfig
{
    @Primary
    @Bean("defaultHttpSecurity")
    public HttpSecurity defaultHttpSecurity(HttpSecurity httpSecurity)
    {
        return httpSecurity;
    }
}
