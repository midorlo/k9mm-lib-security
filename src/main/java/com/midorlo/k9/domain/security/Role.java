package com.midorlo.k9.domain.security;

import com.midorlo.k9.domain.security.property.SecurityDomainConstants;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static com.midorlo.k9.domain.security.property.SecurityDomainConstants.*;

@Entity
@Table(name = ROLES)

@Getter
@Setter
@RequiredArgsConstructor

public class Role {

    @Id
    @Column(name = SecurityDomainConstants.ID)
    private Long id;

    @Column(name = SecurityDomainConstants.NAME,
            nullable = false,
            unique = true
    )
    private String name;

    @ManyToMany(fetch = FetchType.EAGER,
                cascade = { CascadeType.PERSIST,
                            CascadeType.MERGE })
    @JoinTable(name = REL_ROLES_CLEARANCES,
               joinColumns = @JoinColumn(name = REL_ROLES_CLEARANCES_ROLE,
                                         referencedColumnName = ID),
               inverseJoinColumns = @JoinColumn(name = REL_ROLES_CLEARANCES_CLEARANCE,
                                                referencedColumnName = ID))
    @ToString.Exclude
    private Set<Clearance> clearances;

    //<editor-fold desc="Constructors">
    public Role(String name, Set<Clearance> clearances) {
        this.name       = name;
        this.clearances = clearances;
    }

    public Role(String name, Clearance clearance) {
        this(name, Set.of(clearance));
    }

    public Role(String name) {
        this(name, new HashSet<>());
    }
    //</editor-fold>
}