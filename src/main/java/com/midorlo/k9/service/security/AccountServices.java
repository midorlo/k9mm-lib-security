package com.midorlo.k9.service.security;

import com.midorlo.k9.repository.security.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountServices {

    private final AccountRepository accountRepository;

    public AccountServices(AccountRepository accountRepository) {this.accountRepository = accountRepository;}
}
