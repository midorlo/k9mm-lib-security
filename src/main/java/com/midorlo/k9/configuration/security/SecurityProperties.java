package com.midorlo.k9.configuration.security;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class SecurityProperties {

    @Value("${k9.security.issuer}")
    private String issuer;

    @Value("${k9.security.key}")
    private String key;
}
