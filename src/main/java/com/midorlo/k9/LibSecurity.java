package com.midorlo.k9;

import com.midorlo.k9.domain.security.Account;
import com.midorlo.k9.domain.security.Authority;
import com.midorlo.k9.domain.security.RestResourceMetadata;
import com.midorlo.k9.domain.security.Role;
import com.midorlo.k9.domain.security.property.AccountState;
import com.midorlo.k9.service.security.AccountService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;

@SpringBootApplication
@EnableConfigurationProperties
@Slf4j
public class LibSecurity implements ApplicationListener<ContextRefreshedEvent> {

    public static void main(String[] args) {
        SpringApplication.run(LibSecurity.class, args);
    }

    @Bean
    public CommandLineRunner initialize(
            AccountService accountService
    ) {
        return args -> {
            log.info("Initializing Module Security");

            Authority devRegGetAuthority = new Authority(HttpMethod.GET, new RestResourceMetadata("/dev/reg"));
            Authority devOpGetAuthority = new Authority(HttpMethod.GET, new RestResourceMetadata("/dev/op"));

            Role userRole = new Role("User", devRegGetAuthority);
            Role adminRole = new Role("Administrator", devOpGetAuthority);

            Account userAccount = accountService.createIfNotExists(new Account("user", "user@localhost", "user",
                    AccountState.ACTIVATED, userRole));
            Account adminAccount = accountService.createIfNotExists(new Account("admin", "admin@localhost", "admin",
                    AccountState.ACTIVATED, adminRole));

            log.info("Created Accounts {}, {} and theirs complete access models", userAccount, adminAccount);
        };
    }

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        RequestMappingHandlerMapping requestMappingHandlerMapping = applicationContext
                .getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping
                .getHandlerMethods();
        map.forEach((key, value) -> log.info("{} {}", key, value));
    }
}
