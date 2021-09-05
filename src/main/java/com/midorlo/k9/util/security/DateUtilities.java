package com.midorlo.k9.util.security;

import java.util.Date;

public class DateUtilities {

    private DateUtilities(){}

    public static Date now() {
        return new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
    }

    public static Date inOneWeek() {
        return new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000);
    }

    public static Date inOneMonth() {
        return new Date(System.currentTimeMillis() + 4L * 7 * 24 * 60 * 60 * 1000);
    }
}
