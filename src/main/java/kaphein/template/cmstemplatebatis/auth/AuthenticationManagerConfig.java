package kaphein.template.cmstemplatebatis.auth;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthenticationManagerConfig
{
    @Bean
    public AuthenticationManager authenticationManager() throws Exception
    {
        return httpSecurity
            .getSharedObject(AuthenticationManagerBuilder.class)
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder)
            .and()
            .build();
    }

    @Resource(name = "defaultHttpSecurity")
    public void setHttpSecurity(HttpSecurity httpSecurity)
    {
        this.httpSecurity = httpSecurity;
    }

    @Resource(type = PasswordEncoder.class)
    public void setPasswordEncoder(PasswordEncoder passwordEncoder)
    {
        this.passwordEncoder = passwordEncoder;
    }

    @Resource(type = UserDetailsService.class)
    public void setUserDetailsService(UserDetailsService userDetailsService)
    {
        this.userDetailsService = userDetailsService;
    }

    private HttpSecurity httpSecurity;

    private PasswordEncoder passwordEncoder;

    private UserDetailsService userDetailsService;
}
