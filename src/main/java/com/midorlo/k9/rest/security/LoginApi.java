package com.midorlo.k9.rest.security.ops.security;

import com.midorlo.k9.service.security.AccountService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginApi {

    private final AccountService accountService;

    public LoginApi(AccountService accountService) {this.accountService = accountService;}

}
