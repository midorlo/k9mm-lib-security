package com.midorlo.k9.web.rest.security;

import com.midorlo.k9.domain.security.Account;
import com.midorlo.k9.service.security.AccountsService;
import com.midorlo.k9.service.security.dto.LoginDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/login")
public class AuthenticationController {

    private final AccountsService accountsService;

    public AuthenticationController(AccountsService accountsService) {
        this.accountsService = accountsService;
    }

    @PostMapping()
    public ResponseEntity<Account> login(@RequestBody @Valid LoginDto request) {
        try {
            Authentication authenticate = accountsService.authenticate(request);
            Account account = (Account) authenticate.getPrincipal();
            return ResponseEntity.ok()
                    .header(
                            HttpHeaders.AUTHORIZATION,
                            accountsService.generateAccessToken(account)
                    )
                    .body(account); //.body(userViewMapper.toUserView(user)); todo implement
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
