package com.midorlo.k9.model.security.mapper;

import com.midorlo.k9.domain.security.Account;
import com.midorlo.k9.model.security.LoginDto;
import org.springframework.lang.NonNull;

/**
 * Maps any account related dto to an account pojo. Validates against field defaults.
 */
public class AccountMapper {

    private AccountMapper() {}

    /**
     * Maps given dto into an unidentified account <b>with plain password</b>.
     */
    public static Account toAccount(@NonNull LoginDto dto) {
        return new Account(
                dto.getName(),
                dto.getEmail(),
                dto.getPassword()
        );
    }
}
