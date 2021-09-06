package com.midorlo.k9.repository.security;

import com.midorlo.k9.domain.security.RestResourceMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface RestMetaRepository extends JpaRepository<RestResourceMetadata, Long> {
    Optional<RestResourceMetadata> findByServletPathEqualsIgnoreCase(@NonNull String servletPath);
}
