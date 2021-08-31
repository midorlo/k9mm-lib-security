package com.midorlo.k9.domain.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Role extends AbstractAuditingK9Entity {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(name = "roles_authorities",
            joinColumns = @JoinColumn(name = "id_role", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_authority", referencedColumnName = "id"))
    @ToString.Exclude
    private Set<Authority> authorities;

    public Role(String name, Set<Authority> authorities) {
        this.name = name;
        this.authorities = authorities;
    }

    public Role(String name, Authority authority) {
        this(name, Set.of(authority));
    }

    public Role(String name) {
        this(name, new HashSet<>());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Role role = (Role) o;
        return Objects.equals(id, role.id);
    }
}
