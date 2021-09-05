package com.midorlo.k9.exception.security;

import com.midorlo.k9.exception.security.util.K9SecurityException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class BadRequestK9SecurityException extends K9SecurityException {

    public BadRequestK9SecurityException() {
    }

    public BadRequestK9SecurityException(String message) {
        super(message);
    }
}