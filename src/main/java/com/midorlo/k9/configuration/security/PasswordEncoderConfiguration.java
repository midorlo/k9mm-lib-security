package com.midorlo.k9.configuration.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
public class PasswordEncoderConfiguration {

    /**
     * Configure password encoding schema
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("passwordEncoder()");
        return new BCryptPasswordEncoder();
    }
}
