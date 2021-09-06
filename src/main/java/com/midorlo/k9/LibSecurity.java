package com.midorlo.k9;

import com.midorlo.k9.configuration.security.SecurityConstants;
import com.midorlo.k9.domain.core.ServletPath;
import com.midorlo.k9.domain.security.Account;
import com.midorlo.k9.domain.security.Authority;
import com.midorlo.k9.domain.security.Role;
import com.midorlo.k9.domain.security.property.AccountState;
import com.midorlo.k9.repository.security.RoleRepository;
import com.midorlo.k9.service.security.AccountService;
import com.midorlo.k9.service.security.EndpointService;
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
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableConfigurationProperties
@Slf4j
public class LibSecurity implements ApplicationListener<ContextRefreshedEvent> {

    public static void main(String[] args) {
        SpringApplication.run(LibSecurity.class, args);
    }

    @Bean
    public CommandLineRunner initialize(
            AccountService accountService,
            EndpointService endpointService,
            RoleRepository roleRepository
                                       ) {
        return args -> {

            log.info("Initializing Servlet Paths");
            Map<ServletPath, Set<Authority>> pathsAndAuthorities = new HashMap<>();


            for (String moduleServletPath : SecurityConstants.getModuleServletPaths()) {
                Map<ServletPath, Set<Authority>> servletPathSetMap =
                        endpointService.generateServletPathWithDefaultAuthorities(moduleServletPath);
                pathsAndAuthorities.putAll(servletPathSetMap);
            }
            Set<Authority> allAuthorities = pathsAndAuthorities.values()
                                                               .stream()
                                                               .flatMap(Collection::stream)
                                                               .collect(Collectors.toSet());
            Set<ServletPath> servletPaths = pathsAndAuthorities.keySet();

            Role             userRole     = roleRepository.save(new Role("User"));
            Role             adminRole    = roleRepository.save(new Role("Administrator"));

            userRole.getAuthorities().addAll(allAuthorities);
            Role save = roleRepository.save(userRole);

            Account userAccount = accountService.createIfNotExists(
                    new Account("user", "user@localhost", "user", AccountState.ACTIVATED, userRole));
            Account adminAccount = accountService.createIfNotExists(
                    new Account("admin", "admin@localhost", "admin", AccountState.ACTIVATED, adminRole));

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
