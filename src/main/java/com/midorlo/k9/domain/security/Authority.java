package com.midorlo.k9.domain.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "authorities")
public class Authority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;


    @Enumerated
    @Column(name = "http_method", nullable = false)
    private HttpMethod method;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH}, optional = false)
    @JoinColumn(name = "endpoint_id", nullable = false)
    private Endpoint endpoint;

    public Authority() {
    }

    public Authority(HttpMethod method, Endpoint endpoint) {
        this.method = method;
        this.endpoint = endpoint;
    }

    @Override
    public String getAuthority() {
        return endpoint.getServletPath() + ":" + method;
    }
}
