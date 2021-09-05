package com.midorlo.k9.exception.security;

import io.jsonwebtoken.security.SecurityException;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown on unauthorized Request.
 * <h5>todo collect metadata</h5>
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class UnauthorizedK9SecurityException extends SecurityException {

    static final String MSG = "Authorization failed for method {1} on servlet path {2} using authentication {3}";

    public UnauthorizedK9SecurityException(Authentication authentication, String servletPath, HttpMethod httpMethod) {
        super(MessageFormatter.arrayFormat(MSG, new Object[]{servletPath, httpMethod, authentication}).getMessage());
    }
}
