package com.midorlo.k9.service.security;

import com.midorlo.k9.model.security.AuditorAwareUserDetailsImpl;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;

import static com.midorlo.k9.configuration.core.ApplicationConstants.SPRING_SECURITY_OVERRIDE;

/**
 * UserDetailsService is described as a core interface that loads user-specific data in the Spring documentation.
 * <p>
 * In most use cases, authentication providers extract user identity information based on credentials from a database
 * and then perform validation. Because this use case is so common, Spring developers decided to extract it as a
 * separate interface, which exposes the single function:
 * <p>
 * loadUserByUsername accepts username as a parameter and returns the user identity object.
 */
@Slf4j
@Component(SPRING_SECURITY_OVERRIDE)
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountService accountService;

    public UserDetailsServiceImpl(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Loads a Spring Security User.
     *
     * @param email unique user name.
     * @return Spring Security UserDetails Pojo
     */
    @Override
    public @NonNull UserDetails loadUserByUsername(final String email) {
        return accountService
                .findAccountByEmail(email)
                .map(AuditorAwareUserDetailsImpl::new)
                .orElseThrow(EntityNotFoundException::new);
    }
}
