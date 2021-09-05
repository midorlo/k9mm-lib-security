package com.midorlo.k9.configuration.security;

import com.midorlo.k9.exception.K9UncheckedException;
import com.midorlo.k9.exception.security.BadRequestK9SecurityException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SecurityExceptionControllerAdvice {

    static String DEFAULT_MSG =
            "In the words of lyrical coryphaeus Farid Hamed El Abdellaoui: Fickst du mit mir?  Dann fickst du mit mir!";

    @ExceptionHandler(value = BadRequestK9SecurityException.class)
    public ResponseEntity<Object> exception(BadRequestK9SecurityException exception) {
        return new ResponseEntity<>(DEFAULT_MSG, HttpStatus.BAD_REQUEST);
    }
}
