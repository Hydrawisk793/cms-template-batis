package kaphein.template.cmstemplatebatis.auth;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kaphein.template.cmstemplatebatis.persistence.TransactionService;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

public class AuthenticatedUserInterceptor
    implements HandlerInterceptor
{
    public AuthenticatedUserInterceptor(
        TransactionService transactionService,
        AuthorizationService authorizationService
    )
    {
        this.transactionService = Objects.requireNonNull(transactionService);
        this.authorizationService = Objects.requireNonNull(authorizationService);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
    {
        if(isControllerHandlerMethod(handler))
        {
            final Object loggedInUser = transactionService
                .doReadCommittedWithResult((txStatus) -> authorizationService
                    .findLoggedInUserFrom(
                        SecurityContextHolder.getContext().getAuthentication(),
                        txStatus
                    )
                );

            request.setAttribute(LOGGED_IN_ADMIN_MEMBER_REQUEST_ATTRIBUTE_NAME, loggedInUser);
        }

        return true;
    }

    @Override
    public void postHandle(
        HttpServletRequest request, HttpServletResponse response, Object handler,
        @Nullable ModelAndView modelAndView
    )
    {
        if(isControllerHandlerMethod(handler))
        {
            if(null != modelAndView)
            {
                if(!isRedirectView(modelAndView))
                {
                    final Object loggedInUser = request.getAttribute(LOGGED_IN_ADMIN_MEMBER_REQUEST_ATTRIBUTE_NAME);
                    if(null != loggedInUser)
                    {
                        modelAndView.addObject(LOGGED_IN_ADMIN_MEMBER_ATTRIBUTE_NAME, loggedInUser);
                    }
                }
            }
        }
    }

    private static final String LOGGED_IN_ADMIN_MEMBER_ATTRIBUTE_NAME = "loggedInUser";

    private static final String LOGGED_IN_ADMIN_MEMBER_REQUEST_ATTRIBUTE_NAME = String.format(
        "%s.%s",
        AuthenticatedUserInterceptor.class.getName(),
        LOGGED_IN_ADMIN_MEMBER_ATTRIBUTE_NAME
    );

    private boolean isControllerHandlerMethod(Object handler)
    {
        return handler instanceof HandlerMethod;
    }

    private boolean isRedirectView(ModelAndView modelAndView)
    {
        final String viewName = modelAndView.getViewName();
        boolean result = null != viewName && viewName.startsWith("redirect:");
        if(!result)
        {
            final View view = modelAndView.getView();
            result = null != view && RedirectView.class.isAssignableFrom(view.getClass());
        }

        return result;
    }

    private final TransactionService transactionService;

    private final AuthorizationService authorizationService;
}
