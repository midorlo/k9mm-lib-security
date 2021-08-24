package com.midorlo.k9.configuration.security.filters;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Authorizes a {@link HttpServletRequest} and continues the filter chain.
 */
@Slf4j
public class AuthorizationFilter extends OncePerRequestFilter {

    @Override
    @SneakyThrows
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    FilterChain chain) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String         servletPath    = request.getServletPath();
        log.info("Security Context: {}, {}", servletPath, authentication);
        chain.doFilter(request, response);
    }
}