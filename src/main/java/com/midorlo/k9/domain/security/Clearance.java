package com.midorlo.k9.domain.security;

import com.midorlo.k9.domain.core.Servlet;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.springframework.http.HttpMethod;

import javax.persistence.*;

import static com.midorlo.k9.domain.security.property.SecurityDomainConstants.*;
import static org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE;

@Getter
@Setter
@NoArgsConstructor

@Table(name = CLEARANCES_TABLE)
@Entity(name = CLEARANCES_ENTITY)

@Cache(usage = NONSTRICT_READ_WRITE)

public class Clearance {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = CLEARANCES_METHOD,
            nullable = false,
            updatable = false
    )
    private HttpMethod method;

    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.DETACH }, optional = false)
    @JoinColumn(name = "servlet_name", nullable = false)
    protected Servlet servlet;
}
