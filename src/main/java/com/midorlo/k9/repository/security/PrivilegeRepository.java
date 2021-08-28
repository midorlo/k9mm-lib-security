package com.midorlo.k9.repository.security;

import com.midorlo.k9.domain.security.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
}
