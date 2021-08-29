package com.midorlo.k9.domain.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "privileges")
public class Authority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "path", nullable = false, unique = true)
    private String absolutePath;

    @Enumerated
    @Column(name = "http_method", nullable = false)
    private HttpMethod httpMethod;

    @Override
    public String getAuthority() {
        return httpMethod + " " + absolutePath;
    }
}
