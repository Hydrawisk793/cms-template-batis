package kaphein.template.cmstemplatebatis.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import kaphein.template.cmstemplatebatis.PathUtils;
import kaphein.template.cmstemplatebatis.web.ProtocolProperties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;

@Configuration
public class AuthenticationConfig
{
    @PostConstruct
    public void onPostConstruct() throws Exception
    {
        final String loginPagePath = "/admin/login";
        final String loginProtocolPath = PathUtils.join("/", protocolProperties.getPrefix(), "/authentication/login");
        final String logoutProtocolPath = PathUtils.join("/", protocolProperties.getPrefix(), "/authentication/logout");

        httpSecurity
            .authenticationManager(authenticationManager);

        httpSecurity
            .exceptionHandling()
            .authenticationEntryPoint((request, response, authException) ->
            {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                final RequestDispatcher dispatcher = request.getRequestDispatcher("/error");
                dispatcher.forward(request, response);
            });
            //.accessDeniedHandler(null);

        httpSecurity
            .logout()
            .logoutUrl(logoutProtocolPath)
            .clearAuthentication(true)
            .logoutSuccessUrl(loginPagePath)
            .deleteCookies("JSESSIONID")
            .invalidateHttpSession(true)
            .permitAll();

        httpSecurity
            .formLogin()
            .loginProcessingUrl(loginProtocolPath)
            .usernameParameter("loginName")
            .passwordParameter("loginPassword")
            .successHandler((request, response, authentication) -> response.sendRedirect(request.getContextPath() + "/admin/dashboard"))
            .failureHandler(((request, response, exception) -> response.sendRedirect(request.getContextPath() + "/login#failed")))
            .permitAll();

        httpSecurity
            .rememberMe()
            .rememberMeParameter("rememberMe")
            .rememberMeCookieName("rememberMe")
            .key(REMEMBER_ME_KEY)
            .tokenValiditySeconds(30 * 24 * 60 * 60)
            .userDetailsService(userDetailsService)
            .tokenRepository(persistentTokenRepository);
    }

    @Resource(name = "defaultHttpSecurity")
    public void setHttpSecurity(HttpSecurity httpSecurity)
    {
        this.httpSecurity = httpSecurity;
    }

    @Resource(type = ProtocolProperties.class)
    public void setProtocolProperties(ProtocolProperties protocolProperties)
    {
        this.protocolProperties = protocolProperties;
    }

    @Resource(type = UserDetailsService.class)
    public void setUserDetailsService(UserDetailsService userDetailsService)
    {
        this.userDetailsService = userDetailsService;
    }

    @Resource(type = AuthenticationManager.class)
    public void setAuthenticationManager(AuthenticationManager authenticationManager)
    {
        this.authenticationManager = authenticationManager;
    }

    @Resource(type = PersistentTokenRepository.class)
    public void setPersistentTokenRepository(PersistentTokenRepository persistentTokenRepository)
    {
        this.persistentTokenRepository = persistentTokenRepository;
    }

    /**
     * 매 프로젝트마다 변경할 것.
     */
    private static final String REMEMBER_ME_KEY = "ivAppIg9QgfUE92pNLNebFhdgl7MU0aCUbnoVOmDqlsfnniN3lX07GxJMnX8J4MO";

    private HttpSecurity httpSecurity;

    private ProtocolProperties protocolProperties;

    private UserDetailsService userDetailsService;

    private AuthenticationManager authenticationManager;

    private PersistentTokenRepository persistentTokenRepository;
}
