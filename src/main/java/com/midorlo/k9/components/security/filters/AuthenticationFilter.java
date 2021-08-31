package com.midorlo.k9.components.security.filters;

import com.midorlo.k9.service.security.AccountsService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Sets the security context of any request and passes back the filter chain.
 */
@Slf4j
@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    private final AccountsService accountsService;

    public AuthenticationFilter(AccountsService accountsService) {
        this.accountsService = accountsService;
    }

    @Override
    @SneakyThrows
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    FilterChain chain) {
        accountsService.authenticate(request);
        chain.doFilter(request, response);
    }
}