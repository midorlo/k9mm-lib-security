package com.midorlo.k9.service.security;

import com.midorlo.k9.domain.security.Account;
import com.midorlo.k9.repository.security.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final RoleService       roleService;

    public AccountService(AccountRepository accountRepository, RoleService roleService) {
        this.accountRepository = accountRepository;
        this.roleService       = roleService;
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
                                                        roleService.createIfNotExists(role)
                                                )
                                        )
                                );
    }

    public Optional<Account> findAccountByEmail(String email) {
        return accountRepository.findAccountByEmail(email);
    }
}
