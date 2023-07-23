package kaphein.template.cmstemplatebatis.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import kaphein.template.cmstemplatebatis.PathUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Configuration
public class ProtocolSecurityConfig
{
    @PostConstruct
    public void onPostConstruct() throws Exception
    {
        final String pattern = PathUtils.join("/", properties.getPrefix(), "**");

        httpSecurity
            .authorizeHttpRequests((auth) -> auth.antMatchers(pattern).permitAll());

        urlBasedCorsConfigurationSource.registerCorsConfiguration(pattern, corsConfiguration);
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

    @Resource(type = ProtocolProperties.class)
    public void setProperties(ProtocolProperties properties)
    {
        this.properties = properties;
    }

    @Resource(name = "protocolCorsConfiguration")
    public void setCorsConfiguration(CorsConfiguration corsConfiguration)
    {
        this.corsConfiguration = corsConfiguration;
    }

    private HttpSecurity httpSecurity;

    private UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource;

    private ProtocolProperties properties;

    private CorsConfiguration corsConfiguration;
}
