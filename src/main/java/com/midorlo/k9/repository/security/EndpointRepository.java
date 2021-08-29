package com.midorlo.k9.repository.security;

import com.midorlo.k9.domain.security.Endpoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface EndpointRepository extends JpaRepository<Endpoint, Long> {
    Optional<Endpoint> findByServletPathEqualsIgnoreCase(@NonNull String servletPath);




}
