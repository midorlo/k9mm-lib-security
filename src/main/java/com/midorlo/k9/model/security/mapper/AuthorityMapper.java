package com.midorlo.k9.model.security.mapper;

import com.midorlo.k9.domain.security.Account;
import com.midorlo.k9.model.security.spring.GrantedAuthorityImpl;

import java.util.Collection;
import java.util.stream.Collectors;

public class AuthorityMapper {

    public static Collection<GrantedAuthorityImpl> getAuthorities(Account account) {
        return account.getClearances()
                .stream()
                .map(GrantedAuthorityImpl::new)
                .collect(Collectors.toList());

    }
}
