package com.midorlo.k9.config.security.filters;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Authorizes a {@link HttpServletRequest} and continues the filter chain.
 */
@Slf4j
@Component
public class AuthorizationFilterImpl extends AuthorizationFilter {

    private final AuthorizationManager<HttpServletRequest> authorizationManager;

    /**
     * Creates an instance.
     *
     * @param authorizationManager the {@link AuthorizationManager} to use
     */
    public AuthorizationFilterImpl(AuthorizationManager<HttpServletRequest> authorizationManager) {
        super(authorizationManager);
        this.authorizationManager = authorizationManager;
    }

    @Override
    @SneakyThrows
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
        log.info("doFilterInternal({}, {}, {})", request, response, chain);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String servletPath = request.getServletPath();
        HttpMethod httpMethod = HttpMethod.valueOf(request.getMethod());

//        if (!authorizationService.isAuthorized(httpMethod, servletPath, authentication)) {
//            throw new AuthorizationException(authentication, servletPath, httpMethod);
//        }
        this.authorizationManager.verify(this::getAuthentication, request);
        chain.doFilter(request, response);
    }

    private Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new AuthenticationCredentialsNotFoundException(
                    "An Authentication object was not found in the SecurityContext");
        }
        return authentication;
    }
}