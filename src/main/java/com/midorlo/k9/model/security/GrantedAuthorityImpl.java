package com.midorlo.k9.model.security;

import com.midorlo.k9.domain.security.Authority;
import org.springframework.security.core.GrantedAuthority;

public class GrantedAuthorityImpl implements GrantedAuthority {

    private final Authority authority;

    public GrantedAuthorityImpl(Authority authority) {this.authority = authority;}

    @Override
    public String getAuthority() {
        return authority.getRestResourceMetadata().getServletPath() + ":" + authority.getMethod();
    }
}
