package com.midorlo.k9;

import com.midorlo.k9.domain.security.Account;
import com.midorlo.k9.domain.security.Authority;
import com.midorlo.k9.domain.security.Endpoint;
import com.midorlo.k9.domain.security.Role;
import com.midorlo.k9.domain.security.property.AccountState;
import com.midorlo.k9.service.security.AccountsService;
import com.midorlo.k9.service.security.AuthorityService;
import com.midorlo.k9.service.security.EndpointService;
import com.midorlo.k9.service.security.RolesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

import java.util.Set;

@SpringBootApplication
@EnableConfigurationProperties
@Slf4j
public class InitSecurity {

    public static void main(String[] args) {
        SpringApplication.run(InitSecurity.class, args);
    }

    @Bean
    public CommandLineRunner initialize(
            EndpointService endpointService,
            RolesService rolesService,
            AccountsService accountsService,
            AuthorityService authorityService
    ) {
        return args -> {
            log.info("Initializing Module Security");

            Endpoint regEndpoint = new Endpoint("/dev/reg");
            Endpoint adminEndpoint = new Endpoint("/dev/op");

            Authority devRegGetAuthority = new Authority(HttpMethod.GET, regEndpoint);
            Authority devOpGetAuthority = new Authority(HttpMethod.GET, adminEndpoint);

            Role userRole = new Role("User", devRegGetAuthority);
            Role adminRole = new Role("Administrator", devOpGetAuthority);

            Account userAccount = new Account("user@localhost", "user", userRole, AccountState.ACTIVE);
            Account adminAccount = new Account("admin@localhost", "admin", adminRole, AccountState.ACTIVE);

            accountsService.createIfNotExists(userAccount);
            accountsService.createIfNotExists(adminAccount);

//            regEndpoint = endpointService.createEndpointIfNotExists(regEndpoint);
//            adminEndpoint = endpointService.createEndpointIfNotExists(adminEndpoint);
//
//            devRegGetAuthority = authorityService.createIfNotExists(devRegGetAuthority);
//            devOpGetAuthority = authorityService.createIfNotExists(devOpGetAuthority);







            log.info("Initialized Module Security");
        };
    }
}
