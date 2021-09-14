package com.midorlo.k9.service.security.internal;

import com.midorlo.k9.domain.security.Account;
import com.midorlo.k9.model.security.spring.UserDetailsImpl;
import com.midorlo.k9.repository.security.AccountRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import static com.midorlo.k9.service.security.internal.UserDetailsServiceImpl.SPRING_SECURITY_OVERRIDE;

import javax.persistence.EntityNotFoundException;

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

    static final String SPRING_SECURITY_OVERRIDE = "userDetailsService";

    private final AccountRepository accountRepository;

    public UserDetailsServiceImpl(AccountRepository accountRepository) {this.accountRepository = accountRepository;}

    /**
     * Loads a Spring Security User. (The semantics are forced.)
     *
     * @param login translates to {@link Account#getLogin()}
     * @return see {@link UserDetails}
     */
    @Override
    public @NonNull UserDetails loadUserByUsername(final String login) {
        return accountRepository
                .findAccountByLogin(login)
                .map(UserDetailsImpl::new)
                .orElseThrow(EntityNotFoundException::new);
    }
}
