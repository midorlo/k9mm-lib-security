package com.midorlo.k9;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties

@Slf4j
public class LibSecurity
//        implements ApplicationListener<ContextRefreshedEvent>
{

    public static void main(String[] args) {
        SpringApplication.run(LibSecurity.class, args);
    }

//    @Bean
//    public CommandLineRunner initialize(
//            AccountService accountService,
//            EndpointService endpointService,
//            RoleRepository roleRepository
//                                       ) {
//        return args -> {
//
//            log.info("Initializing Servlet Paths");
//            Map<ServletDescription, Set<Authority>> pathsAndAuthorities = new HashMap<>();
//
//
////            for (String moduleServletPath : SecurityConstants.getModuleServletPaths()) {
////                Map<ServletDescription, Set<Authority>> servletPathSetMap =
////                        endpointService.generateServletPathWithDefaultAuthorities(moduleServletPath);
////                pathsAndAuthorities.putAll(servletPathSetMap);
////            }
////            Set<Authority> allAuthorities = pathsAndAuthorities.values()
////                                                               .stream()
////                                                               .flatMap(Collection::stream)
////                                                               .collect(Collectors.toSet());
////            Set<ServletDescription> servletDescriptions = pathsAndAuthorities.keySet();
////
////            Role userRole  = roleRepository.save(new Role("User"));
////            Role adminRole = roleRepository.save(new Role("Administrator"));
////
////            userRole.getAuthorities().addAll(allAuthorities);
////            Role save = roleRepository.save(userRole);
////
////            Account userAccount  = accountService.createIfNotExists(new Account("Default User"));
////            Account adminAccount = accountService.createIfNotExists(new Account("System"));
////
////            log.info("Created Accounts {}, {} and theirs complete access models", userAccount, adminAccount);
//        };
//    }

//    @Override
//    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
//        ApplicationContext applicationContext = event.getApplicationContext();
//        RequestMappingHandlerMapping requestMappingHandlerMapping = applicationContext
//                .getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
//        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping
//                .getHandlerMethods();
//        map.forEach((key, value) -> log.info("{} {}", key, value));
//    }
}
