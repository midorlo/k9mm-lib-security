package com.midorlo.k9.domain.security;

import com.midorlo.k9.domain.security.property.AccountState;
import lombok.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity(name = "account")
@Table(name = "accounts", schema = "security")
@RequiredArgsConstructor
public class Account extends AuditorAwareK9Entity {

    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    @ToString.Exclude
    private String password;



    @Enumerated
    @Column(name = "state", nullable = false)
    private AccountState state;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(name = "accounts_roles",
            joinColumns = @JoinColumn(name = "id_account", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_role", referencedColumnName = "id"))
    @ToString.Exclude
    private Collection<Role> roles = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(name = "accounts_authorities",
            joinColumns = @JoinColumn(name = "id_account", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_privilege", referencedColumnName = "id"))
    @ToString.Exclude
    private Set<Authority> authorities = new HashSet<>();

    public Account(String email,
                   String password,
                   Role primaryRole,
                   AccountState state) {
        this.email = email;
        this.password = password;
        this.roles.add(primaryRole);
        this.state = state;
    }

    @Override
    @NonNull
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", state=" + state +
                ", roles=" + roles +
                ", authorities=" + authorities +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id) && Objects.equals(email, account.email) && Objects.equals(password, account.password) && state == account.state && Objects.equals(roles, account.roles) && Objects.equals(authorities, account.authorities);
    }
}
