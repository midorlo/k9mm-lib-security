package com.midorlo.k9.domain.security;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "roles")
public class Role {

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    public Long getId()        {return id;}

    public void setId(Long id) {this.id = id;}


    @ManyToMany
    @JoinTable(name = "roles_privileges",
            joinColumns = @JoinColumn(name = "id_role"),
            inverseJoinColumns = @JoinColumn(name = "id_privilege"))
    private Collection<Privilege> privileges;


    public String getName()          {return name;}

    public void setName(String name) {this.name = name;}

    public Collection<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Collection<Privilege> privileges) {
        this.privileges = privileges;
    }
}
