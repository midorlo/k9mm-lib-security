package com.midorlo.k9.domain.security;

import com.midorlo.k9.domain.security.util.AuditorAwareK9Entity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.springframework.http.HttpMethod;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = Authority.ENTITY_NAME)
@Getter
@Setter
@RequiredArgsConstructor
public class Authority extends AuditorAwareK9Entity {

    public static final String ENTITY_NAME = "authorities";
    public static final String COLUMN_METHOD_NAME = "http_method";
    @Enumerated(EnumType.STRING)
    @Column(name = COLUMN_METHOD_NAME, nullable = false)
    private HttpMethod method;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH },
            optional = false)
    @JoinColumn(name = "id_rest_meta", nullable = false)
    private RestMeta restMeta;

    public Authority(HttpMethod method, RestMeta restMeta) {
        this.method = method;
        this.restMeta = restMeta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Authority authority = (Authority) o;
        return Objects.equals(getId(), authority.getId());
    }

    public String getAuthority() {
        return restMeta.getServletPath() + ":" + method;
    }
}
