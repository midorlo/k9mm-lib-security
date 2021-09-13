package com.midorlo.k9.model.security;

import com.midorlo.k9.domain.security.Account;
import com.midorlo.k9.domain.security.Authority;
import com.midorlo.k9.domain.security.Role;
import com.midorlo.k9.domain.security.property.AccountState;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthoritiesContainer;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getGrantedAuthorities();
    }

    @Override
    public Collection<? extends GrantedAuthority> getGrantedAuthorities() {
        Stream<Authority> authorityStream = account.getAuthorities()
                                                   .stream();
        Stream<Authority> rolesAuthoritiesStream = account.getRoles()
                                                          .stream()
                                                          .map(Role::getAuthorities)
                                                          .flatMap(Collection::stream);
        return Stream.concat(authorityStream, rolesAuthoritiesStream)
                     .map(GrantedAuthorityImpl::new)
                     .collect(Collectors.toList());
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