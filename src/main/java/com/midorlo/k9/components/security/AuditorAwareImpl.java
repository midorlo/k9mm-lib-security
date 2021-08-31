package com.midorlo.k9.components.security;

import com.midorlo.k9.domain.security.Account;
import com.midorlo.k9.service.security.AccountsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Implementation of {@link AuditorAware} based on Spring Security.
 */
@Component
@Slf4j
public class AuditorAwareImpl implements AuditorAware<Account> {

    public static final String AUDITOR_AWARE_IMPL_INSTANCE_NAME = "auditorAwareImpl";

    private final AccountsService accountsService;

    public AuditorAwareImpl(AccountsService accountsService) {
        this.accountsService = accountsService;
    }


    @Override
    @NonNull
    public Optional<Account> getCurrentAuditor() {
        String currentAuditorAccountname = accountsService.getPrincipal().orElse("System@localhost");
        return accountsService.findAccountByEmail(currentAuditorAccountname);
    }
}
