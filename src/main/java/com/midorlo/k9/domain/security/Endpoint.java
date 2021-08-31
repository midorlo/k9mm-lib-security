package com.midorlo.k9.domain.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "endpoints")
@Getter
@Setter
@RequiredArgsConstructor
public class Endpoint extends AbstractAuditingK9Entity {

    public Endpoint(String servletPath) {
        this.servletPath = servletPath;
    }

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "servlet_path", nullable = false, unique = true, length = 128)
    private String servletPath;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (getClass() != o.getClass()) return false;
        Endpoint endpoint = (Endpoint) o;
        return Objects.equals(id, endpoint.id) && Objects.equals(servletPath, endpoint.servletPath);
    }

    @Override
    public String toString() {
        return "Endpoint{" +
                "id=" + id +
                ", servletPath='" + servletPath + '\'' +
                '}';
    }
}
