package com.midorlo.k9.domain.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;

import javax.persistence.*;

import static com.midorlo.k9.domain.security.property.SecurityDomainConstants.CLEARANCES_ENTITY;
import static com.midorlo.k9.domain.security.property.SecurityDomainConstants.CLEARANCES_TABLE;
import static org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE;

@Getter
@Setter
@NoArgsConstructor

@Table(name = CLEARANCES_TABLE)
@Entity(name = CLEARANCES_ENTITY)

@Cache(usage = NONSTRICT_READ_WRITE)

public class Clearance {

    private static final long serialVersionUID = 1L;
    public static final String COLUMN_PATH_NAME = "path";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = COLUMN_PATH_NAME, nullable = false, unique = true)
    protected String path;

}
