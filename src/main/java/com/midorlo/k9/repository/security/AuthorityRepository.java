package com.midorlo.k9.repository.security;

import com.midorlo.k9.domain.security.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpMethod;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    Optional<Authority> findByHttpMethodEqualsAndAbsolutePathEqualsIgnoreCase(HttpMethod httpMethod, String absolutePath);
}
