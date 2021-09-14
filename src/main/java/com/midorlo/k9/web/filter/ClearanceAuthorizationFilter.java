package com.midorlo.k9.web.filter;

import com.midorlo.k9.exception.security.UnauthorizedK9SecurityException;
import com.midorlo.k9.model.security.spring.GrantedAuthorityImpl;
import com.midorlo.k9.model.security.spring.UserDetailsImpl;
import com.midorlo.k9.service.security.ClearanceServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;


@Slf4j
@Component
public class ClearanceAuthorizationFilter extends OncePerRequestFilter {

    private final ClearanceServices clearanceServices;

    public ClearanceAuthorizationFilter(ClearanceServices clearanceServices) {
        this.clearanceServices = clearanceServices;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {


        clearanceServices.getRequiredClearance(request.getRequestURI())
                         .ifPresent(clearance -> {

                             boolean isAuthorized = false;

                             Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                             if (authentication != null) {
                                 UserDetailsImpl principal =
                                         (UserDetailsImpl) authentication.getPrincipal();

                                 Collection<? extends GrantedAuthority> grantedAuthorities =
                                         principal.getGrantedAuthorities();

                                 GrantedAuthorityImpl grantedAuthority = new GrantedAuthorityImpl(clearance);
                                 if (grantedAuthorities.contains(grantedAuthority)) {
                                     isAuthorized = true;
                                 }
                             }

                             if (!isAuthorized) {
                                 throw new AuthorizationServiceException("Unauthorized!");
                             }

                         });
        filterChain.doFilter(request, response);
    }
}
