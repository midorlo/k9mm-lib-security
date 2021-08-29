package com.midorlo.k9.components.security;

import com.midorlo.k9.repository.security.AuthorityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.function.Supplier;

@Slf4j
@Component
public class AuthorizationManagerImpl implements AuthorizationManager<HttpServletRequest> {

    private final AuthorityRepository authorityRepository;

    public AuthorizationManagerImpl(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Override
    public void verify(Supplier<Authentication> authentication, HttpServletRequest object) {
        AuthorizationManager.super.verify(authentication, object);
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authenticationSupplier, HttpServletRequest request) {
        log.info("check({}, {})", authenticationSupplier, request);

        Authentication authentication = resolveSupplier(authenticationSupplier);
        boolean result = authorityRepository
                .findByHttpMethodEqualsAndAbsolutePathEqualsIgnoreCase(
                        HttpMethod.valueOf(request.getMethod()),
                        request.getServletPath())
                .map(authority -> authentication != null && authentication.getAuthorities()
                        .stream().map(GrantedAuthority::getAuthority)
                        .anyMatch(s -> s.equals(authority.getAuthority())))
                .orElse(true);
        log.info("Result: " + result);
        return new AuthorizationDecision(result);
    }

    private Authentication resolveSupplier(Supplier<Authentication> authenticationSupplier) {
        Authentication authentication = null;
        try {
            authentication = authenticationSupplier.get();
        } catch (Exception ignored) {
        }
        return authentication;
    }
}
