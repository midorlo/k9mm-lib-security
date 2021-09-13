package com.midorlo.k9.domain.security;

import com.midorlo.k9.domain.security.property.AccountState;
import com.midorlo.k9.domain.security.util.AuditableEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.midorlo.k9.domain.security.Account.*;

/**
 * Represents an Account for this Security Module.
 */
@Getter
@Setter
@Entity(name = ENTITY)
@Table(name = TABLE, schema = SCHEMA)
@RequiredArgsConstructor
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@ToString
public class Account extends AuditableEntity<Long> implements IAccount {

    static final String TABLE  = "accounts";
    static final String SCHEMA = "security";
    static final String ENTITY = "account";

    static final String COLUMN_ID            = "id";
    static final String COLUMN_DISPLAY_NAME  = "name";
    static final String COLUMN_LOGIN         = "login";
    static final String COLUMN_PASSWORD_HASH = "password";
    static final String COLUMN_STATE         = "state";

    static final String RELATION_TO_ROLES        = "accounts_roles";
    static final String RELATION_TO_CLEARANCES   = "accounts_roles";
    static final String TO_ROLES_SELF            = "id_account";
    static final String TO_ROLES_OTHER           = "id_role";
    static final String TO_CLEARANCES_SELF_NAME  = "id_account";
    static final String TO_CLEARANCES_OTHER_NAME = "id_role";

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = COLUMN_DISPLAY_NAME, nullable = false, unique = true)
    private String displayName;

    @Column(name = COLUMN_LOGIN, nullable = false, unique = true, length = 64)
    private String login;

    @Column(name = COLUMN_PASSWORD_HASH, nullable = false, length = 128)
    private String passwordHash;

    @Enumerated
    @Column(name = COLUMN_STATE, nullable = false, length = 2)
    private AccountState state;

    @ManyToMany(cascade = { CascadeType.MERGE })
    @JoinTable(name = RELATION_TO_ROLES,
               joinColumns = @JoinColumn(name = TO_ROLES_SELF, referencedColumnName = COLUMN_ID),
               inverseJoinColumns = @JoinColumn(name = TO_ROLES_OTHER, referencedColumnName = COLUMN_ID))
    @ToString.Exclude
    private Collection<Role> roles = new HashSet<>();

    @ToString.Exclude
    @ManyToMany(cascade = { CascadeType.MERGE })
    @JoinTable(name = RELATION_TO_CLEARANCES,
               joinColumns = @JoinColumn(name = TO_CLEARANCES_SELF_NAME, referencedColumnName = COLUMN_ID),
               inverseJoinColumns = @JoinColumn(name = TO_CLEARANCES_OTHER_NAME, referencedColumnName = COLUMN_ID))
    private Set<Authority> authorities = new HashSet<>();

    //<editor-fold desc="Constructor">

    public Account(String displayName) {
        this.displayName = displayName;
    }

    public Account(String displayName,
                   String passwordHash,
                   AccountState state,
                   Collection<Role> roles,
                   Set<Authority> authorities) {
        this.displayName  = displayName;
        this.passwordHash = passwordHash;
        this.state        = state;
        this.roles.addAll(roles);
        this.authorities.addAll(authorities);
    }

    public Account(String login,
                   String passwordHash,
                   AccountState state,
                   Role primaryRole) {
        this.login        = login;
        this.displayName  = login;
        this.passwordHash = passwordHash;
        this.state        = state;
        this.roles.add(primaryRole);
    }

    public Account(String displayName,
                   String login,
                   String passwordHash) {
        this.displayName  = displayName;
        this.login        = login;
        this.passwordHash = passwordHash;
    }

    public Account(String displayName,
                   String login) {
        this.displayName = displayName;
        this.login       = login;
    }

    public Account(String displayName,
                   String login,
                   String passwordHash,
                   AccountState state,
                   Collection<Role> roles) {
        this.displayName  = displayName;
        this.login        = login;
        this.passwordHash = passwordHash;
        this.state        = state;
        this.roles        = roles;
    }

    public Account(String displayName,
                   String login,
                   String passwordHash,
                   AccountState state,
                   @NonNull Set<Role> roles,
                   @NonNull Set<Authority> authorities) {
        this.displayName  = displayName;
        this.login        = login;
        this.passwordHash = passwordHash;
        this.state        = state;
        this.roles        = roles;
        this.authorities  = authorities;
    }

    public Account(String displayName,
                   String login,
                   String passwordHash,
                   AccountState state,
                   @NonNull Role primaryRole,
                   @NonNull Set<Authority> authorities) {
        this.displayName  = displayName;
        this.login        = login;
        this.passwordHash = passwordHash;
        this.state        = state;
        this.authorities  = authorities;
        this.roles        = new HashSet<>();
        roles.add(primaryRole);
    }

    //</editor-fold>
}
