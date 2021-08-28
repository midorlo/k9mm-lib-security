package com.midorlo.k9.web.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
public class SecurityController {

    @RequestMapping("/public")
    public ResponseEntity<String> pub() {
        return new ResponseEntity<>("Batman!", HttpStatus.OK);
    }

    @RequestMapping("/secured")
    public ResponseEntity<String> secure() {
        return new ResponseEntity<>("Batman!", HttpStatus.OK);

    }

    @RequestMapping("/ops")
    public ResponseEntity<String> ops() {
        return new ResponseEntity<>("Batman!", HttpStatus.OK);

    }
}
