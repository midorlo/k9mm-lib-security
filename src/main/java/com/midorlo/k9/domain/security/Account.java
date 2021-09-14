package com.midorlo.k9.domain.security;

import com.midorlo.k9.domain.hr.Person;
import com.midorlo.k9.domain.security.property.AbstractAuditable;
import com.midorlo.k9.domain.security.property.SecurityDomainConstants;
import com.midorlo.k9.domain.security.property.AccountState;
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

/**
 * Represents an Account for this Security Module.
 */
@Getter
@Setter
@Entity(name = SecurityDomainConstants.ACCOUNTS_ENTITY)
@Table(name = SecurityDomainConstants.ACCOUNTS_TABLE, schema = SecurityDomainConstants.SCHEMA)
@RequiredArgsConstructor
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@ToString
public class Account extends AbstractAuditable<Long> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = SecurityDomainConstants.ACCOUNTS_ID)
    private Long id;


    @Column(name = SecurityDomainConstants.ACCOUNTS_DISPLAY_NAME,
            nullable = false,
            unique = true
    )
    private String displayName;

    @Column(name = SecurityDomainConstants.ACCOUNTS_LOGIN,
            nullable = false,
            unique = true,
            length = 64
    )
    private String login;

    @Column(name = SecurityDomainConstants.ACCOUNTS_PASSWORD_HASH, nullable = false, length = 128)
    private String passwordHash;

    @Enumerated
    @Column(name = SecurityDomainConstants.ACCOUNTS_STATE, nullable = false, length = 2)
    private AccountState state;

    @ManyToMany(cascade = { CascadeType.MERGE })
    @JoinTable(name = SecurityDomainConstants.REL_ACCOUNTS_ROLES,
               joinColumns = @JoinColumn(name = SecurityDomainConstants.REL_ACCOUNTS_ROLES_ACCOUNT,
                                         referencedColumnName = SecurityDomainConstants.ACCOUNTS_ID),
               inverseJoinColumns = @JoinColumn(name = SecurityDomainConstants.REL_ACCOUNTS_ROLES_ROLE,
                                                referencedColumnName = SecurityDomainConstants.ACCOUNTS_ID))
    @ToString.Exclude
    private Collection<Role> roles = new HashSet<>();

    @ToString.Exclude
    @ManyToMany(cascade = { CascadeType.MERGE })
    @JoinTable(name = SecurityDomainConstants.REL_ACCOUNTS_CLEARANCES,
               joinColumns = @JoinColumn(name = SecurityDomainConstants.REL_ACCOUNTS_CLEARANCES_ACCOUNT,
                                         referencedColumnName = SecurityDomainConstants.ACCOUNTS_ID),
               inverseJoinColumns = @JoinColumn(name = SecurityDomainConstants.REL_ACCOUNTS_CLEARANCES_CLEARANCE,
                                                referencedColumnName = SecurityDomainConstants.ACCOUNTS_ID))
    private Set<Clearance> clearances = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER,
               cascade = { CascadeType.MERGE, CascadeType.DETACH },
               optional = false)
    @JoinColumn(name = SecurityDomainConstants.ID_OWNER,
                nullable = false)
    protected Person owner;

    //<editor-fold desc="Constructor">

    public Account(String displayName) {
        this.displayName = displayName;
    }

    public Account(String displayName,
                   String passwordHash,
                   AccountState state,
                   Collection<Role> roles,
                   Set<Clearance> clearances) {
        this.displayName  = displayName;
        this.passwordHash = passwordHash;
        this.state        = state;
        this.roles.addAll(roles);
        this.clearances.addAll(clearances);
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
                   @NonNull Set<Clearance> clearances) {
        this.displayName  = displayName;
        this.login        = login;
        this.passwordHash = passwordHash;
        this.state        = state;
        this.roles        = roles;
        this.clearances   = clearances;
    }

    public Account(String displayName,
                   String login,
                   String passwordHash,
                   AccountState state,
                   @NonNull Role primaryRole,
                   @NonNull Set<Clearance> clearances) {
        this.displayName  = displayName;
        this.login        = login;
        this.passwordHash = passwordHash;
        this.state        = state;
        this.clearances   = clearances;
        this.roles        = new HashSet<>();
        roles.add(primaryRole);
    }
    //</editor-fold>
}
