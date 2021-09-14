package com.midorlo.k9.configuration.security;

import com.midorlo.k9.domain.security.Account;
import com.midorlo.k9.model.security.spring.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Implementation of {@link AuditorAware} based on Spring Security.
 */
@Component
@Slf4j
public class AuditorAwareImpl implements AuditorAware<Account> {

    public static final String AUDITOR_AWARE_IMPL_INSTANCE_NAME = "auditorAwareImpl";

    @Override
    @NonNull
    public Optional<Account> getCurrentAuditor() {
        return Optional.of(((UserDetailsImpl) SecurityContextHolder.getContext()
                                                                   .getAuthentication()
                                                                   .getPrincipal()).getAccount()); // N⁰ :-)
    }
}
