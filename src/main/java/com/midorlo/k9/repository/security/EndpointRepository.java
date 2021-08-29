package com.midorlo.k9.repository.security;

import com.midorlo.k9.domain.security.Endpoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EndpointRepository extends JpaRepository<Endpoint, Long> {

}
