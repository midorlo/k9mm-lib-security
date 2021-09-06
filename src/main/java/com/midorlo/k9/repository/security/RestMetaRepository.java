package com.midorlo.k9.repository.security;

import com.midorlo.k9.domain.core.ServletPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface RestMetaRepository extends JpaRepository<ServletPath, Long> {
    Optional<ServletPath> findByPath(@NonNull String servletPath);
}
