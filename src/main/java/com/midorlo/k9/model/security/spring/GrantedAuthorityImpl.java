package com.midorlo.k9.model.security.spring;

import com.midorlo.k9.domain.security.Clearance;
import org.springframework.security.core.GrantedAuthority;

public class GrantedAuthorityImpl implements GrantedAuthority {

    private final Clearance clearance;

    public GrantedAuthorityImpl(Clearance clearance) {this.clearance = clearance;}

    @Override
    public String getAuthority() {
        return clearance.getPath();
    }
}
