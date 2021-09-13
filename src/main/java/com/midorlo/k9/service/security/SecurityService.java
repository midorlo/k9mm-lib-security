package com.midorlo.k9.service.security;

import com.midorlo.k9.domain.security.IAccount;
import com.midorlo.k9.repository.security.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class SecurityService implements ISecurityServices {

    private final AccountRepository accountRepository;

    public SecurityService(AccountRepository accountRepository) {this.accountRepository = accountRepository;}

    @Override
    public IAccount getAccount(Long id) {
        return accountRepository.getById(id);
    }
}
