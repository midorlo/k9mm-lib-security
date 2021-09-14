package com.midorlo.k9.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Utility class providing convenient access to k9-security-related functionality.
 */
public final class SecurityUtilities {

    public static final String ADMIN     = "ROLE_ADMIN";
    public static final String USER      = "ROLE_USER";
    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    private SecurityUtilities() {}

    /**
     * Get the login of the current user.
     *
     * @return the login of the current user.
     * @implNote cheap!
     */
    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }

    /**
     * Get the JWT of the current user.
     *
     * @return the JWT of the current user.
     */
    public static Optional<String> getCurrentUserJWT() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                       .filter(authentication -> authentication.getCredentials() instanceof String)
                       .map(authentication -> (String) authentication.getCredentials());
    }

    /**
     * Check if a user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise.
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext()
                                                             .getAuthentication();
        return authentication != null
               && getAuthorities(authentication).noneMatch(ANONYMOUS::equals);
    }

    /**
     * Checks if the current user has a specific authority.
     *
     * @param authority the authority to check.
     * @return true if the current user has the authority, false otherwise.
     */
    public static boolean hasCurrentUserThisAuthority(String authority) {
        Authentication authentication = SecurityContextHolder.getContext()
                                                             .getAuthentication();
        return authentication != null
               && getAuthorities(authentication).anyMatch(authority::equals);
    }

    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
            return springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof String) {
            return (String) authentication.getPrincipal();
        }
        return null;
    }

    private static Stream<String> getAuthorities(Authentication authentication) {
        return authentication.getAuthorities()
                             .stream()
                             .map(GrantedAuthority::getAuthority);
    }
}
