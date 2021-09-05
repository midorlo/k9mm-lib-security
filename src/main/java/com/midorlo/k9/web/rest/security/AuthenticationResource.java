package com.midorlo.k9.web.rest.security;

import com.midorlo.k9.domain.security.Account;
import com.midorlo.k9.model.security.LoginDto;
import com.midorlo.k9.service.security.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/authentication")
public class AuthenticationResource {

    private final AuthenticationService authenticationService;

    public AuthenticationResource(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody @Valid LoginDto dto) {
        return authenticationService.handleLogin(dto);
    }
}
