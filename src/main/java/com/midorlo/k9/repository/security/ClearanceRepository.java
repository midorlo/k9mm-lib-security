package com.midorlo.k9.repository.security;

import com.midorlo.k9.domain.security.Clearance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface ClearanceRepository extends JpaRepository<Clearance, Long> {
    Optional<Clearance> findByPath(@NonNull String path);

    List<Clearance> findByPathContaining(String path);

    List<Clearance> findByPathLike(String path);

    List<Clearance> findByPathStartsWith(String path);



}
