package com.midorlo.k9.domain.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "authorities")
@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class Authority extends AuditorAwareK9Entity implements GrantedAuthority {

    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated
    @Column(name = "http_method", nullable = false)
    private HttpMethod method;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH}, optional = false)
    @JoinColumn(name = "endpoint_id", nullable = false)
    private Endpoint endpoint;

    public Authority(HttpMethod method, Endpoint endpoint) {
        this.method = method;
        this.endpoint = endpoint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Authority authority = (Authority) o;
        return Objects.equals(getId(), authority.getId());
    }

    //<editor-fold desc="Spring Security Implementation">
    @Override
    public String getAuthority() {
        return endpoint.getServletPath() + ":" + method;
    }
    //</editor-fold>
}
