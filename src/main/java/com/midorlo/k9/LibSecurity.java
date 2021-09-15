package com.midorlo.k9;

import com.midorlo.k9.domain.security.Account;
import com.midorlo.k9.service.security.AccountServices;
import com.midorlo.k9.web.rest.security.model.RegistrationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties

@Slf4j
public class LibSecurity {

    public static void main(String[] args) {
        SpringApplication.run(LibSecurity.class, args);
    }


//    @Bean
//    public CommandLineRunner initialize(AccountServices accountServices) {
//        return args -> {
//            RegistrationDto registrationDto = new RegistrationDto();
//            Account admin = accountServices.registerNewAccount(registrationDto.setFirstName("Admin")
//                                                                              .setLastName("Admin")
//                                                                              .setDisplayName("Admin")
//                                                                              .setLogin("Admin")
//                                                                              .setPassword("Admin"));
//            log.info("Created {}", admin);
//
//        };
//    }
}
