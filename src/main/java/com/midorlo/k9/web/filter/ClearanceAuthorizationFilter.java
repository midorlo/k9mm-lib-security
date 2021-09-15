package com.midorlo.k9.web.filter;

import com.midorlo.k9.model.security.spring.GrantedAuthorityImpl;
import com.midorlo.k9.service.security.ClearanceServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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


        clearanceServices.findMatchingClearance(request.getRequestURI())
                         .ifPresent(clearance -> {

                             boolean isAuthorized = false;

                             Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                             if (authentication != null) {
                                 org.springframework.security.core.userdetails.User u =
                                         (User) authentication.getPrincipal();

                                 Collection<? extends GrantedAuthority> grantedAuthorities =
                                         u.getAuthorities();

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
