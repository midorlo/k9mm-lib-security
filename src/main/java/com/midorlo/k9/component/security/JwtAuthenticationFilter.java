package com.midorlo.k9.component.security;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Request modifying filter. Checks for the presence of, and parses Authentication from a jwt.
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final  String AUTHORIZATION_HEADER             = "Authorization";
    public static final  String AUTHORIZATION_HEADER_FIELD_NAME  = "Bearer ";
    private static final int    AUTHORIZATION_HEADER_FIELD_INDEX = AUTHORIZATION_HEADER_FIELD_NAME.length();


    private final JwtProvider jwtProvider;

    public JwtAuthenticationFilter(JwtProvider jwtProvider) {this.jwtProvider = jwtProvider;}

    @Override
    @SneakyThrows
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) {
        String jwt = resolveToken(request);
        if (jwtProvider.isTokenValid(jwt)) {
            Authentication authentication = this.jwtProvider.resolveAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

    private String resolveToken(@NonNull HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        return (StringUtils.hasText(bearerToken) && bearerToken.startsWith(AUTHORIZATION_HEADER_FIELD_NAME))
               ? bearerToken.substring(AUTHORIZATION_HEADER_FIELD_INDEX)
               : null;
    }
}
