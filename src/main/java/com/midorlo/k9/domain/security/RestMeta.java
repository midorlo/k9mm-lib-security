package com.midorlo.k9.domain.security;

import com.midorlo.k9.domain.security.util.AuditorAwareK9Entity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "rest_meta")
@Getter
@Setter
@RequiredArgsConstructor
public class RestMeta extends AuditorAwareK9Entity {

    public RestMeta(String servletPath) {
        this.servletPath = servletPath;
    }

    @Column(name = "servlet_path", nullable = false, unique = true, length = 128)
    private String servletPath;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RestMeta restMeta = (RestMeta) o;
        return Objects.equals(getId(), restMeta.getId());
    }
}
