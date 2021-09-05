package com.midorlo.k9.model.security;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Used by http login requests.
 */
@Data
@RequiredArgsConstructor
public class LoginDto {
    /**
     * Unique name per account.
     */
    private final String name;
    /**
     * Unique email per account.
     */
    private final String email;
    /**
     * Accounts password.
     */
    private final String password;
    /**
     * Jwt session lifetime extension.
     */
    private final boolean remember;
}
