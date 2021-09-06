package com.midorlo.k9.service.security;

import com.midorlo.k9.domain.core.ServletPath;
import com.midorlo.k9.domain.security.Account;
import com.midorlo.k9.domain.security.Authority;
import com.midorlo.k9.domain.security.Role;
import com.midorlo.k9.domain.security.property.AccountState;
import com.midorlo.k9.repository.security.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder   passwordEncoder;

    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder   = passwordEncoder;
    }

    public Account createIfNotExists(Account account) {
        return accountRepository.findAccountByEmail(account.getEmail()).orElse(create(account));
    }

    public Account create(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return accountRepository.save(account);
    }

    public void createAccountWithRoleAndAuthority(String accountName,
                                                  String email,
                                                  String passwordUnencrypted,
                                                  AccountState accountState,
                                                  String roleName,
                                                  String servletPath,
                                                  Collection<HttpMethod> methodsAllowed) {
        ServletPath restResourceMetadata = new ServletPath(servletPath);
        Set<Authority> authorities = methodsAllowed.stream()
                                                   .map(m -> new Authority(m, restResourceMetadata))
                                                   .collect(Collectors.toSet());
        Role role = new Role(roleName, authorities);
        Account account = new Account(accountName,
                                      email,
                                      passwordUnencrypted,
                                      AccountState.ACTIVATED, role,
                                      authorities);
    }


    public Optional<Account> findAccountByEmail(String email) {
        return accountRepository.findAccountByEmail(email);
    }
}