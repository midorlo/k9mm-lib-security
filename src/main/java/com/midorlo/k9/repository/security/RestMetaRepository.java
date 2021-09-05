package com.midorlo.k9.repository.security;

import com.midorlo.k9.domain.security.RestMeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface RestMetaRepository extends JpaRepository<RestMeta, Long> {
    Optional<RestMeta> findByServletPathEqualsIgnoreCase(@NonNull String servletPath);
}
