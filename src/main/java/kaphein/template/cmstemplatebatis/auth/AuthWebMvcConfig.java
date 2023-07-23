package kaphein.template.cmstemplatebatis.auth;

import kaphein.template.cmstemplatebatis.persistence.TransactionService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class AuthWebMvcConfig implements WebMvcConfigurer
{
    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(new AuthenticatedUserInterceptor(transactionService, authorizationService));
    }

    @Resource(name = "transactionService")
    public void setTransactionService(TransactionService transactionService)
    {
        this.transactionService = transactionService;
    }

    @Resource(name = "authorizationService")
    public void setAuthorizationService(AuthorizationService authorizationService)
    {
        this.authorizationService = authorizationService;
    }

    private TransactionService transactionService;

    private AuthorizationService authorizationService;
}
