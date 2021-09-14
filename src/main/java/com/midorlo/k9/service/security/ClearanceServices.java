package com.midorlo.k9.service.security;

import com.midorlo.k9.domain.security.Clearance;
import com.midorlo.k9.repository.AbstractJpaService;
import com.midorlo.k9.repository.security.ClearanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import java.util.Optional;

@Service
public class ClearanceServices extends AbstractJpaService<Clearance, Long> {

    private final AntPathMatcher matcher = new AntPathMatcher();

    public ClearanceServices(ClearanceRepository clearanceRepository) {
        super(clearanceRepository);
    }

    @Override
    public Clearance create(Clearance clearance) {
        findMatchingClearance(clearance.getPath())
                .ifPresent(c -> {
                    throw new RuntimeException(clearance.getPath() + " collides with new clearance path" + clearance.getPath());
                });
        return repository.save(clearance);
    }

    public Optional<Clearance> findMatchingClearance(String requestUri) {
        return repository.findAll()
                         .stream().filter(e -> matcher.match(e.getPath(), requestUri))
                         .findFirst();
    }
}
