package com.midorlo.k9.service.security;

import com.midorlo.k9.domain.security.Account;
import com.midorlo.k9.repository.security.AccountRepository;
import com.midorlo.k9.domain.security.property.AccountState;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountsService  {

    private final AccountRepository accountRepository;
    private final RolesService roleService;

    public AccountsService(AccountRepository accountRepository, RolesService roleService) {
        this.accountRepository = accountRepository;
        this.roleService = roleService;
    }

    public Optional<Account> findByName(String email) {
        return accountRepository.findAccountByEmail(email);
    }

    public Account createIfNotExists(String email, String password, String role) {
        return accountRepository.findAccountByEmail(email)
                .orElse(accountRepository.save(
                                new Account(
                                        email,
                                        password,
                                        roleService.createIfNotExists(role),
                                        AccountState.ACTIVE
                                )
                        )
                );
    }

    public Optional<Account> findAccountByEmail(String email) {
        return accountRepository.findAccountByEmail(email);
    }
}
