package com.midorlo.k9.domain.security;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "privileges")
public class Privilege implements GrantedAuthority {

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "path", nullable = false, unique = true)
    private String path;

    @Enumerated
    @Column(name = "http_method", nullable = false)
    private HttpMethod httpMethod;

    /**
     * Translation for Spring Security.
     *
     * @return name.
     */
    @Override
    public String getAuthority() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Privilege privilege = (Privilege) o;

        return Objects.equals(id, privilege.id);
    }

    @Override
    public int hashCode() {
        return 2090507994;
    }
}
