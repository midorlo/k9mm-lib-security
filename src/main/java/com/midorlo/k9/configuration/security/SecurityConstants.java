package com.midorlo.k9.configuration.security;

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
