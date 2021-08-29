package com.midorlo.k9.service.security;

import com.midorlo.k9.repository.security.AuthorityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthorizationService {

    private final AuthorityRepository authorizationRepository;

    public AuthorizationService(AuthorityRepository authorizationRepository) {
        this.authorizationRepository = authorizationRepository;
    }

    public boolean isAuthorized(HttpMethod httpMethod, String servletPath, Authentication authentication) {
        log.info("isAuthorized({}, {}, {})", authentication, servletPath, httpMethod);
        return true; //todo impl
    }
}
