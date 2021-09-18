package com.midorlo.k9.configuration.security;

import com.midorlo.k9.util.YamlPropertySourceFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "security")
@PropertySource(value = "classpath:security.yml", factory = YamlPropertySourceFactory.class)
public class SecurityProperties {

    private String issuer;
    private String key;
    private Long   validityMs;
    private Long   extendedValidityMs;
}
