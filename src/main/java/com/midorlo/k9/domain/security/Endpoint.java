package com.midorlo.k9.domain.security;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "endpoints")
@Getter
@Setter
public class Endpoint {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "servlet_path", nullable = false, unique = true, length = 128)
    private String servletPath;

    @Column(name = "name", length = 256)
    private String name;

    @Column(name = "description", length = 32)
    private String description;
}
