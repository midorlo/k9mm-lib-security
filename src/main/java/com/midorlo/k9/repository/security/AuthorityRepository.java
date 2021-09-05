package com.midorlo.k9.repository.security;

import com.midorlo.k9.domain.security.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {


    Optional<Authority> findByRestMeta_ServletPathEqualsIgnoreCaseAndMethodEquals(
            @NonNull String servletPath,
            @NonNull HttpMethod method
    );
}
