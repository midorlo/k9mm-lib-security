package com.midorlo.k9.web.rest.security;

import com.midorlo.k9.service.security.AccountsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/security")
public class AuthenticationController {

    private final AccountsService accountsService;

    public AuthenticationController(AccountsService accountsService) {this.accountsService = accountsService;}

}
