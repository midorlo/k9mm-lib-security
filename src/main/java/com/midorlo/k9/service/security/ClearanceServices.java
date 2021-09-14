package com.midorlo.k9.service.security;

import com.midorlo.k9.domain.security.Clearance;
import com.midorlo.k9.repository.security.ClearanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import java.util.Optional;

@SuppressWarnings("unused")
@Service
@RequiredArgsConstructor
public class ClearanceServices {

    private final ClearanceRepository clearanceRepository;
    private final AntPathMatcher      matcher = new AntPathMatcher();

    public Clearance createIfNotExists(Clearance clearance) {
        return clearanceRepository.findByServlet_Path(clearance.getServlet().getPath())
                                  .orElse(clearanceRepository.save(clearance));
    }

    public Optional<Clearance> getRequiredClearance(String requestUri) {

        return clearanceRepository.findAll()
                                  .stream().filter(e -> matcher.match(e.getServlet().getPath(), requestUri))
                                  .findFirst();


    }
}
