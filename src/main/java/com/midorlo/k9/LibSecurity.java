package com.midorlo.k9;

import com.midorlo.k9.domain.apimap.EndpointReference;
import com.midorlo.k9.repository.apimap.EndpointReferenceRepository;
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
@Slf4j
public class LibSecurity {

    @Value("${k9.security.default-admin-login}")
    private String adminEmail;
    @Value("${k9.security.default-admin-password}")
    private String adminPassword;

    @Profile({"dev", "tst", "int", "!prd"})
    public static void main(String[] args) {
        SpringApplication.run(LibSecurity.class, args);
    }

    @Bean
    public CommandLineRunner initialize(AccountService accountService,
                                        EndpointReferenceRepository apiDescriptionRepository) {
        return args -> {
            log.info("init({})", accountService);
            log.info("Initialized Account {}", accountService.createIfNotExists(adminEmail, adminPassword,
                    "Administrator"));


            EndpointReference d1 = new EndpointReference();
            d1.setPath("/security");
            apiDescriptionRepository.save(d1);

            log.info("Initialized");

            EndpointReference d2 = new EndpointReference();
            d1.setPath("/security/login");
            apiDescriptionRepository.save(d1);

            EndpointReference d3 = new EndpointReference();
            d1.setPath("/security/register");
            apiDescriptionRepository.save(d1);


        };
    }
}
