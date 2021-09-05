package com.midorlo.k9.model.security.projection;

import com.midorlo.k9.model.security.JsonWebToken;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object handling account registration.
 */
@Data
@Slf4j
@NoArgsConstructor
public class AccountNonSensitiveVm {

    private String name;
    private String email;
    private JsonWebToken token;
    private List<String> roles = new ArrayList<>();

    public AccountNonSensitiveVm(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public AccountNonSensitiveVm(String name, JsonWebToken token, List<String> roles) {
        this.name = name;
        this.token = token;
        this.roles = roles;
    }
}
