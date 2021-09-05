package com.midorlo.k9.domain.security;

import com.midorlo.k9.domain.security.property.AccountState;
import com.midorlo.k9.domain.security.util.NamedAuditorAwareK9Entity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static com.midorlo.k9.domain.security.Account.Meta.*;

/**
 * Represents an Account for this Security Module.
 */

@Getter
@Setter
@Entity(name = ENTITY_NAME)
@Table(name = TABLE_NAME, schema = SCHEMA_NAME)
@RequiredArgsConstructor
@ToString
public class Account extends NamedAuditorAwareK9Entity {

    private static final long serialVersionUID = 141481953116476081L;

    protected static class Meta {

        static final String TABLE_NAME = "accounts";
        static final String SCHEMA_NAME = "security";
        static final String ENTITY_NAME = "account";

        static final String ID = "id";
        static final String NAME = "name";
        static final String EMAIL = "email";
        static final String PASSWORD = "password";
        static final String STATE = "state";

        static final String ACCOUNTS_AUTHORITIES = "accounts_authorities";
        static final String ACCOUNTS_ROLES = "accounts_roles";
        static final String ID_ACCOUNT = "id_account";
        static final String ID_AUTHORITY = "id_authority";
        static final String ID_ROLE = "id_role";
    }

    @Column(name = EMAIL, nullable = false, unique = true, length = 64)
    private String email;

    @Column(name = PASSWORD, nullable = false, length = 128)
    @ToString.Exclude
    private String password;

    @Enumerated
    @Column(name = STATE, nullable = false, length = 2)
    private AccountState state;

    @ManyToMany(
            cascade = { CascadeType.PERSIST,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.DETACH
            }
    )
    @JoinTable(name = ACCOUNTS_ROLES,
            joinColumns = @JoinColumn(name = ID_ACCOUNT, referencedColumnName = ID),
            inverseJoinColumns = @JoinColumn(name = ID_ROLE, referencedColumnName = ID)
    )
    @ToString.Exclude
    private Collection<Role> roles = new HashSet<>();

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH })
    @JoinTable(name = ACCOUNTS_AUTHORITIES,
            joinColumns = @JoinColumn(name = ID_ACCOUNT, referencedColumnName = ID),
            inverseJoinColumns = @JoinColumn(name = ID_AUTHORITY, referencedColumnName = ID))
    @ToString.Exclude
    private Set<Authority> authorities = new HashSet<>();

    public Account(
            String name,
            String email,
            String password,
            Role primaryRole,
            AccountState state
    ) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.roles.add(primaryRole);
        this.state = state;
    }

    public Account(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Account(
            String name,
            String email,
            String password,
            AccountState state,
            Collection<Role> roles,
            Collection<Authority> authorities
    ) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.roles.addAll(roles);
        this.state = state;
        this.authorities.addAll(authorities);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(email, account.email)
                && Objects.equals(password, account.password)
                && state == account.state
                && Objects.equals(roles, account.roles)
                && Objects.equals(authorities, account.authorities);
    }
}
