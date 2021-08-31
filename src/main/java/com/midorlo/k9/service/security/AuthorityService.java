package com.midorlo.k9.service.security;

import com.midorlo.k9.domain.security.Authority;
import com.midorlo.k9.repository.security.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@SuppressWarnings("unused")
@Service
@RequiredArgsConstructor
public class AuthorityService {

    private final AuthorityRepository authorityRepository;

    public Authority createIfNotExists(Authority authority) {
        return authorityRepository.findByEndpoint_ServletPathEqualsIgnoreCaseAndMethodEquals(authority.getEndpoint().getServletPath(), authority.getMethod())
                .orElse(authorityRepository.save(authority));
    }
}
