package com.midorlo.k9.web.rest.security;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {

    @RequestMapping("/login")
    public ResponseEntity<String> login() {
        return ResponseEntity.ok("200 /login");
    }
}
