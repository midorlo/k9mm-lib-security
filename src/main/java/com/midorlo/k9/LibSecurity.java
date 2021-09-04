package com.midorlo.k9;

import com.midorlo.k9.domain.security.Account;
import com.midorlo.k9.domain.security.Authority;
import com.midorlo.k9.domain.security.Endpoint;
import com.midorlo.k9.domain.security.Role;
import com.midorlo.k9.domain.security.property.AccountState;
import com.midorlo.k9.service.security.AccountsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

@SpringBootApplication
@EnableConfigurationProperties
@Slf4j
public class LibSecurity {

    public static void main(String[] args) {
        SpringApplication.run(LibSecurity.class, args);
    }

    @Bean
    public CommandLineRunner initialize(
            AccountsService accountsService
    ) {
        return args -> {
            log.info("Initializing Module Security");

            Authority devRegGetAuthority = new Authority(HttpMethod.GET, new Endpoint("/dev/reg"));
            Authority devOpGetAuthority = new Authority(HttpMethod.GET, new Endpoint("/dev/op"));

            Role userRole = new Role("User", devRegGetAuthority);
            Role adminRole = new Role("Administrator", devOpGetAuthority);

            Account userAccount = accountsService.createIfNotExists(new Account("user@localhost", "user", userRole, AccountState.ACTIVE));
            Account adminAccount = accountsService.createIfNotExists(new Account("admin@localhost", "admin", adminRole, AccountState.ACTIVE));

            log.info("Created Accounts {}, {} and theirs complete access models", userAccount, adminAccount);
        };
    }
}
