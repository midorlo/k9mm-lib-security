package com.midorlo.k9.web.rest.security;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dev/")
public class DeveloperController {

    @RequestMapping("/pub")
    public ResponseEntity<String> pub() {
        return ResponseEntity.ok("200 /pub");
    }

    @RequestMapping("/reg")
    public ResponseEntity<String> reg() {
        return ResponseEntity.ok("200 /reg");
    }
    @RequestMapping("/op")
    public ResponseEntity<String> op() {
        return ResponseEntity.ok("200 /op");
    }
}
