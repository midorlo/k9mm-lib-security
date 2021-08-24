package com.midorlo.k9;

import com.midorlo.k9.service.security.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@EnableConfigurationProperties
@Profile({"dev", "tst", "int"})
@Slf4j
public class LibrarySecurityJwt {

    @Value("${k9.security.default-admin-login}")
    private String adminEmail;
    @Value("${k9.security.default-admin-password}")
    private String adminPassword;

    public static void main(String[] args) {
        SpringApplication.run(LibrarySecurityJwt.class, args);
    }

    @Bean
    public CommandLineRunner init(AccountService accountService) {
        return args -> {
            log.info("init({})", accountService);
            log.info("Initialized Account {}", accountService.createIfNotExists(adminEmail, adminPassword,
                    "Administrator"));
        };
    }
}
