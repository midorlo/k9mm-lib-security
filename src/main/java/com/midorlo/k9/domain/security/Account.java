package com.midorlo.k9.domain.security;

import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @implNote Full implementation of {@link UserDetails}
 */
@Entity
@Table(name = "accounts")
public class Account implements UserDetails {

    public Account() {
    }

    public Account(String email,
                   String password,
                   Role primaryRole) {
        this.email = email;
        this.password = password;
        this.isActivated = true;
        this.isLocked = false;
        this.isExpired = false;
        this.roles.add(primaryRole);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "is_expired", nullable = false)
    private boolean isExpired = false;

    @Column(name = "is_activated", nullable = false)
    private boolean isActivated;

    @Column(name = "is_locked", nullable = false)
    private boolean isLocked = false;

    @ManyToMany
    @JoinTable(name = "accounts_roles",
            joinColumns = @JoinColumn(name = "id_account"),
            inverseJoinColumns = @JoinColumn(name = "id_role"))
    private Collection<Role> roles = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "accounts_privileges",
            joinColumns = @JoinColumn(name = "id_account"),
            inverseJoinColumns = @JoinColumn(name = "id_privilege"))
    private Collection<Privilege> privileges = new HashSet<>();

    public Collection<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Collection<Privilege> privileges) {
        this.privileges = privileges;
    }

    protected Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Collection<? extends Privilege> getAuthorities() {
        return Stream.concat(getRoles().stream().map(Role::getPrivileges),
                        getPrivileges().stream())
                .map(p -> (Privilege) p)
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActivated;
    }

    @Override
    public boolean isEnabled() {
        return !isExpired
                && !isLocked
                && isActivated;
    }
}
