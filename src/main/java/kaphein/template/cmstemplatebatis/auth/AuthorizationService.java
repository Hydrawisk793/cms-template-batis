package kaphein.template.cmstemplatebatis.auth;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import kaphein.template.cmstemplatebatis.persistence.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;

import kaphein.template.cmstemplatebatis.model.Action;
import kaphein.template.cmstemplatebatis.model.ActionRepository;
import kaphein.template.cmstemplatebatis.model.AppUser;
import kaphein.template.cmstemplatebatis.model.AppUserRepository;
import kaphein.template.cmstemplatebatis.model.Permission;
import kaphein.template.cmstemplatebatis.model.PermissionRepository;

/**
 *  회원 인증(로그인)을 처리한다.
 *  엔드포인트 접근 제어는 `canPerformAction` 메소드를 호출하여 수행할 수 있다.
 */
@Service
public class AuthorizationService
{
    public AuthorizationService(
        TransactionService transactionService,
        AppUserRepository appUserRepository,
        PermissionRepository permissionRepository,
        ActionRepository actionRepository
    )
    {
        this.transactionService = Objects.requireNonNull(transactionService);
        this.appUserRepository = Objects.requireNonNull(appUserRepository);
        this.permissionRepository = Objects.requireNonNull(permissionRepository);
        this.actionRepository = Objects.requireNonNull(actionRepository);
    }

    public AppUser findLoggedInUserFrom(Authentication authentication)
    {
        AppUser appUser = null;

        if(isAuthenticated(authentication))
        {
            appUser = transactionService.doReadCommittedWithResult((txStatus) -> appUserRepository.findOneByLoginName(authentication.getName(), txStatus));
        }

        return appUser;
    }

    /**
     *  TODO : 캐싱 적용
     */
    public AppUser findLoggedInUserFrom(Authentication authentication, TransactionStatus txStatus)
    {
        AppUser appUser = null;

        if(isAuthenticated(authentication))
        {
            appUser = appUserRepository.findOneByLoginName(authentication.getName(), txStatus);
        }

        return appUser;
    }

    public Set<String> findPermissionNamesFrom(Authentication authentication)
    {
        Stream<String> permissionStream = Stream.empty();

        if(isAuthenticated(authentication))
        {
            final Object principal = authentication.getPrincipal();
            if(principal instanceof UserDetails)
            {
                final UserDetails userDetails = (UserDetails)principal;
                permissionStream = userDetails
                    .getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority);
            }
        }

        return permissionStream.collect(Collectors.toSet());
    }

    public boolean hasPermissions(Authentication authentication, String... permissions)
    {
        boolean result = isAuthenticated(authentication);

        if(result)
        {
            final Set<String> permissionsOwned = findPermissionNamesFrom(authentication);

            for(String permission : permissions)
            {
                result = permissionsOwned.contains(permission);
                if(!result)
                {
                    break;
                }
            }
        }

        return result;
    }

    public boolean canPerformAction()
    {
        return canPerformAction(SecurityContextHolder.getContext().getAuthentication(), getCurrentHttpServletRequest());
    }

    /**
     *  TODO : 캐싱 적용 및 최적화
     */
    public boolean canPerformAction(Authentication authentication, HttpServletRequest request)
    {
        boolean result = null != authentication && null != request;

        if(result)
        {
            final Object handler = request.getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE);
            result = handler instanceof HandlerMethod;

            if(result)
            {
                final String pathPattern = (String)request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
                final String basePath = extractBasePath(pathPattern);
                final Action action = transactionService.doReadCommittedWithResult((txStatus) -> actionRepository.findOneByActionUrl(basePath, txStatus));
                if(LOGGER.isDebugEnabled())
                {
                    LOGGER.debug("pathPattern == {}, basePath == {}", pathPattern, basePath);

                    if(null != action)
                    {
                        LOGGER.debug("actionUid == {}, permissionUid == {}", action.getActionUid(), action.getPermissionUid());
                    }
                }

                if(null != action)
                {
                    final Long permissionUid = action.getPermissionUid();
                    final Permission permission = transactionService.doReadCommittedWithResult((txStatus) -> permissionRepository.findOneByPermissionUid(permissionUid, txStatus));

                    if(null != permission)
                    {
                        final String permissionName = permission.getPermissionName();
                        result = isAuthenticated(authentication) && findPermissionNamesFrom(authentication).contains(permissionName);

                        if(LOGGER.isDebugEnabled())
                        {
                            LOGGER.debug("member {} has{} permission {}", findLoggedInUserFrom(authentication).getLoginName(), (result ? "" : " not"), permissionName);
                        }
                    }
                    else
                    {
                        LOGGER.debug("action {} does not require any permissions", action.getActionUid());
                    }
                }
            }
        }

        return result;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationService.class);

    private static HttpServletRequest getCurrentHttpServletRequest()
    {
        HttpServletRequest request = null;

        final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if(requestAttributes instanceof ServletRequestAttributes)
        {
            request = ((ServletRequestAttributes)requestAttributes).getRequest();
        }

        return request;
    }

    private static boolean isAuthenticated(Authentication authentication)
    {
        return null != authentication && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

    /**
     *  TODO : 알고리즘 개선
     */
    private String extractBasePath(String pathPattern)
    {
        String basePath = pathPattern;

        final int index = pathPattern.indexOf("/{");
        if(index > 0)
        {
            basePath = pathPattern.substring(0, pathPattern.indexOf("/{"));
        }

        return basePath;
    }

    private final TransactionService transactionService;

    private final AppUserRepository appUserRepository;

    private final PermissionRepository permissionRepository;

    private final ActionRepository actionRepository;
}
