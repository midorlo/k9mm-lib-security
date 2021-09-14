package com.midorlo.k9.configuration.security;

import java.net.URI;

public class SecurityConstants {

    public static final String LOGIN_REGEX = "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|" +
                                             "(?>[_.@A-Za-z0-9-]+)$";


    public static final String DEFAULT_ADMIN_ACCOUNT_MAIL = "admin@localhost";
    public static final String DEFAULT_ADMIN_ACCOUNT_NAME = "admin";
    public static final String DEFAULT_ADMIN_ACCOUNT_PASS = "admin";

    public static final String DEV                  = "/dev";
    public static final String DEV_PUB              = "/dev/pub";
    public static final String DEV_REG              = "/dev/reg";
    public static final String DEV_OP               = "/dev/op";
    public static final String AUTHENTICATION       = "/authentication";
    public static final String AUTHENTICATION_LOGIN = "/authentication/login";
    public static final String ERR_CONCURRENCY_FAILURE   = "error.concurrencyFailure";
    public static final String ERR_VALIDATION            = "error.validation";
    public static final String PROBLEM_BASE_URL          = "https://www.jhipster.tech/problem";
    public static final URI    LOGIN_ALREADY_USED_TYPE   = URI.create(PROBLEM_BASE_URL + "/login-already-used");
    public static final URI    EMAIL_ALREADY_USED_TYPE   = URI.create(PROBLEM_BASE_URL + "/email-already-used");
    public static final URI    INVALID_PASSWORD_TYPE     = URI.create(PROBLEM_BASE_URL + "/invalid-password");
    public static final URI CONSTRAINT_VIOLATION_TYPE = URI.create(PROBLEM_BASE_URL + "/constraint-violation");
    public static final URI DEFAULT_TYPE              = URI.create(PROBLEM_BASE_URL + "/problem-with-message");
    public static final String AUTHORIZATION_HEADER            = "Authorization";
    public static final String AUTHORIZATION_HEADER_FIELD_NAME = "Bearer ";
    public static final int AUTHORIZATION_HEADER_FIELD_INDEX = AUTHORIZATION_HEADER_FIELD_NAME.length();

    public static String[] getModuleServletPaths() {
        return new String[]{
                DEV,
                DEV_PUB,
                DEV_REG,
                DEV_OP,
                AUTHENTICATION,
                AUTHENTICATION_LOGIN
        };
    }
}
