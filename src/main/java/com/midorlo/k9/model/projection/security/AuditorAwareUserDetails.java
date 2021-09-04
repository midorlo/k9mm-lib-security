package com.midorlo.k9.model.projection.security;

import com.midorlo.k9.domain.security.Account;
import com.midorlo.k9.domain.security.property.AccountState;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class AuditorAwareUserDetails implements UserDetails {

    private final Account account;
    private final Collection<? extends GrantedAuthority> grantedAuthorities;

    public AuditorAwareUserDetails(Account Account) {
        this(Account, new ArrayList<>());
    }

    public AuditorAwareUserDetails(Account Account, Collection<? extends GrantedAuthority> grantedAuthorities) {
        this.account = Account;
        this.grantedAuthorities = grantedAuthorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        return account.getEmail();
    }


    @Override
    public boolean isAccountNonExpired() {
        return account.getState().equals(AccountState.ACTIVE);}

    @Override
    public boolean isAccountNonLocked() {
        return account.getState().getValue() >= AccountState.SANCTIONED.getValue();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return account.getState().getValue() >= AccountState.EXPIRED_PASSWORD.getValue();
    }

    @Override
    public boolean isEnabled() {
        return account.getState().getValue() >= AccountState.DISABLED.getValue();
    }

    public Account getAccount() {
        return account;
    }
}