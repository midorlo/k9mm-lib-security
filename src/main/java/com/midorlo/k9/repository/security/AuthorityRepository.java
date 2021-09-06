package com.midorlo.k9.repository.security;

import com.midorlo.k9.domain.security.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    @Query("select a from authorities a where upper(a.servletPath.path) = upper(:servletPath) and a.method = :method")
    Optional<Authority> findByServletPathPathEqualsIgnoreCaseAndMethodEquals(@Param("servletPath") @NonNull String servletPath,
                                                                             @Param("method") @NonNull HttpMethod method);
}
