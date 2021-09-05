package com.midorlo.k9.exception.security.util;

import com.midorlo.k9.exception.K9UncheckedException;

/**
 * Parent Exception for the security module.
 */
public abstract class K9SecurityException extends K9UncheckedException {

    public K9SecurityException() {
    }

    public K9SecurityException(String message) {
        super(message);
    }
}
