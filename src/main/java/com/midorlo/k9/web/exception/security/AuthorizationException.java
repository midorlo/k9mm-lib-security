package com.midorlo.k9.web.exception.security;

import io.jsonwebtoken.security.SecurityException;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;

/**
 * Thrown on unauthorized Request.
 * <h5>todo collect metadata</h5>
 */
public class AuthorizationException extends SecurityException {

    static final String MSG = "Authorization failed for method {1} on servlet path {2} using authentication {3}";

    public AuthorizationException(Authentication authentication, String servletPath, HttpMethod httpMethod) {
        super(MessageFormatter.arrayFormat(MSG, new Object[]{servletPath, httpMethod, authentication}).getMessage());
    }
}
