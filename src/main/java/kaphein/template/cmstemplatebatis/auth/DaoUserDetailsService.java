package kaphein.template.cmstemplatebatis.auth;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import kaphein.template.cmstemplatebatis.persistence.TransactionService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import kaphein.template.cmstemplatebatis.model.AppUser;
import kaphein.template.cmstemplatebatis.model.AppUserRepository;
import kaphein.template.cmstemplatebatis.model.UserRoleEntry;
import kaphein.template.cmstemplatebatis.model.UserRoleEntryRepository;
import kaphein.template.cmstemplatebatis.model.RolePermissionEntryRepository;

@Service
public class DaoUserDetailsService implements UserDetailsService
{
    public DaoUserDetailsService(
        TransactionService transactionService,
        AppUserRepository appUserRepository,
        UserRoleEntryRepository userRoleEntryRepository,
        RolePermissionEntryRepository rolePermissionEntryRepository
    )
    {
        this.transactionService = Objects.requireNonNull(transactionService);
        this.appUserRepository = Objects.requireNonNull(appUserRepository);
        this.userRoleEntryRepository = Objects.requireNonNull(userRoleEntryRepository);
        this.rolePermissionEntryRepository = Objects.requireNonNull(rolePermissionEntryRepository);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        UserDetails userDetails = null;

        final AppUser appUser = findOneByLoginName(username);
        if(null != appUser)
        {
            userDetails = new UserDetailsImpl(
                appUser.getLoginName(),
                appUser.getLoginPassword(),
                permissionsToGrantedAuthorities(appUser.getUserUid())
            );
        }

        return userDetails;
    }

    /**
     * {@link UserDetails}는 Spring Context가 refresh될 때 (주로 재기동) deserialize 되므로
     * 구현체는 반드시 serializable 하여야 하며, 구현체 객체를 초기화할 때는 로그인 당시 인증 정보의 snapshot 데이터를 가지고 있어야 한다.
     *
     * TODO : Outdated 사용자 정보 이슈 해결 방안 모색
     */
    private static class UserDetailsImpl implements UserDetails
    {
        public UserDetailsImpl(
            String loginName,
            String loginPassword,
            Collection<GrantedAuthority> authorities
        )
        {
            this.loginName = loginName;
            this.loginPassword = loginPassword;
            this.authorities = authorities;
        }

        @Override
        public Collection<GrantedAuthority> getAuthorities()
        {
            return authorities;
        }

        @Override
        public String getPassword()
        {
            return loginPassword;
        }

        @Override
        public String getUsername()
        {
            return loginName;
        }

        @Override
        public boolean isAccountNonExpired()
        {
            return true;
        }

        @Override
        public boolean isAccountNonLocked()
        {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired()
        {
            return true;
        }

        @Override
        public boolean isEnabled()
        {
            return true;
        }

        private static final long serialVersionUID = 3637386658305023365L;

        private final String loginName;

        private final String loginPassword;

        private final Collection<GrantedAuthority> authorities;
    }

    private AppUser findOneByLoginName(String loginName)
    {
        return transactionService.doReadCommittedWithResult((txStatus) -> appUserRepository.findOneByLoginName(loginName, txStatus));
    }

    // TODO : 캐싱 및 최적화
    private Set<GrantedAuthority> permissionsToGrantedAuthorities(Long userUid)
    {
        return transactionService
            .doReadCommittedWithResult((txStatus) -> rolePermissionEntryRepository
                .findByRoleNameIn(
                    userRoleEntryRepository
                        .findByUserUidIn(Collections.singletonList(userUid), txStatus)
                        .stream()
                        .map(UserRoleEntry::getRoleName)
                        .collect(Collectors.toSet()),
                    txStatus
                )
                .stream()
                .map((rolePermissionEntry) -> new SimpleGrantedAuthority(rolePermissionEntry.getPermissionName()))
                .collect(Collectors.toSet())
            );
    }

    private final TransactionService transactionService;

    private final AppUserRepository appUserRepository;

    private final UserRoleEntryRepository userRoleEntryRepository;

    private final RolePermissionEntryRepository rolePermissionEntryRepository;
}
