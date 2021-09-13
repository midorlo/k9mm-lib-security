package com.midorlo.k9.web.exception;

import com.midorlo.k9.configuration.security.ErrorConstants;
import org.zalando.problem.Status;

/**
 * <a href="https://tools.ietf.org/html/rfc7807">RFC 7807</a> compliant description of a {@link Status#BAD_REQUEST}
 * problem
 */
public class EmailAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public EmailAlreadyUsedException() {
        super(ErrorConstants.EMAIL_ALREADY_USED_TYPE, "Email is already in use!", "userManagement", "emailexists");
    }
}
