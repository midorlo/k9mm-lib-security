package com.midorlo.k9.model.security.spring;

import com.midorlo.k9.domain.security.Account;
import com.midorlo.k9.domain.security.Clearance;
import com.midorlo.k9.domain.security.Role;
import com.midorlo.k9.domain.security.property.AccountState;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthoritiesContainer;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Explicit implementation of {@link UserDetails}. That way we can maintain a complexity-free callback to
 * any user's {@link Account} record.
 */
public class UserDetailsImpl implements UserDetails,
                                        GrantedAuthoritiesContainer {

    private final Account account;

    public UserDetailsImpl(Account Account) {
        this.account = Account;
    }

    /**
     * Callback to the module's account record.
     *
     * @implNote By doing so, we achieve 0-complexity access to out entity from within
     * {@link org.springframework.security.core.context.SecurityContext}
     */
    public Account getAccount() {
        return account;
    }

    /**
     * Gets all clearances of this account, direct as well as role inherited.
     *
     * @return current clearances.
     */
    @Override
    public Collection<? extends GrantedAuthority> getGrantedAuthorities() {
        Stream<Clearance> directClearances = account.getClearances()
                                                    .stream();
        Stream<Clearance> rolesClearances = account.getRoles()
                                                   .stream()
                                                   .map(Role::getClearances)
                                                   .flatMap(Collection::stream);
        return Stream.concat(directClearances, rolesClearances)
                     .distinct()
                     .map(GrantedAuthorityImpl::new)
                     .collect(Collectors.toList());
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getGrantedAuthorities();
    }

    @Override
    public String getPassword() {
        return account.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return account.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return isEnabled();
    }

    @Override
    public boolean isAccountNonLocked() {
        return isEnabled();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isEnabled();
    }

    @Override
    public boolean isEnabled() {
        return Objects.equals(account.getState().getValue(), AccountState.ACTIVATED.getValue());
    }
}