package com.midorlo.k9.components.security;

import com.midorlo.k9.service.security.AuthenticationService;
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

    private final AuthenticationService authenticationService;

    public AuthenticationFilter(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    @SneakyThrows
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    FilterChain chain) {
        authenticationService.authenticate(request);
        chain.doFilter(request, response);
    }
}

/*
@Override
            public void doFilter(
                    ServletRequest request,
                    ServletResponse response,
                    FilterChain chain) throws IOException, ServletException {

                String token = resolveToken((HttpServletRequest) request);
                if (StringUtils.hasText(token) && authenticationService.isAuthorized(token)) {
                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authenticationService.findAuthentication(token));
                }
                chain.doFilter(request, response);
            }

private String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(applicationProperties.getSecurity().getAuthorization().getHeaderName());
    return StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")
            ? bearerToken.substring(7)
            : null;
}
 */