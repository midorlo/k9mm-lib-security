package com.midorlo.k9.domain.security.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
public abstract class NamedAuditorAwareK9Entity extends AuditorAwareK9Entity {

    private static final long serialVersionUID = 141481953116476081L;
    public static final String COLUMN_NAME_NAME = "name";

    @Column(name = COLUMN_NAME_NAME, nullable = false, unique = true, length = 64)
    protected String name;

    /**
     * todo this may be a perfect spot for injecting an insert into a transaction history
     */
    @PrePersist
    @PreRemove
    @PreUpdate
    void x() {
        System.out.println(getClass().getSimpleName() + " - " + getId() + " -> " + this);
    }
}
