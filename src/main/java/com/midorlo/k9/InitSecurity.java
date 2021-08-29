package com.midorlo.k9;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@EnableConfigurationProperties
@Slf4j
public class InitSecurity {

    @Value("${k9.security.default-admin-login}")
    private String adminEmail;
    @Value("${k9.security.default-admin-password}")
    private String adminPassword;

    @Profile({"dev", "tst", "int", "!prd"})
    public static void main(String[] args) {
        SpringApplication.run(InitSecurity.class, args);
    }
//
//    @Bean
//    public CommandLineRunner initialize(AccountsService accountsService,
//                                        EndpointRepo apiDescriptionRepository) {
//        return args -> {
//            log.info("init({})", accountsService);
//            log.info("Initialized Account {}", accountsService.createIfNotExists(adminEmail, adminPassword,
//                    "Administrator"));
//
//
//            EndpointReference d1 = new EndpointReference();
//            d1.setPath("/security");
//            apiDescriptionRepository.save(d1);
//
//            log.info("Initialized");
//
//            EndpointReference d2 = new EndpointReference();
//            d1.setPath("/security/login");
//            apiDescriptionRepository.save(d1);
//
//            EndpointReference d3 = new EndpointReference();
//            d1.setPath("/security/register");
//            apiDescriptionRepository.save(d1);
//
//
//        };
//    }
}
