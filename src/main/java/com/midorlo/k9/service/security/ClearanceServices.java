package com.midorlo.k9.service.security;

import com.midorlo.k9.domain.security.Clearance;
import com.midorlo.k9.repository.security.ClearanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.Optional;

@SuppressWarnings("unused")
@Service
@RequiredArgsConstructor
public class ClearanceServices {

    private final ClearanceRepository clearanceRepository;

    public Clearance createIfNotExists(Clearance clearance) {
        return clearanceRepository.findByServletPathAndMethod(clearance.getServlet()
                                                                       .getPath(),
                                                              clearance.getMethod())
                                  .orElse(clearanceRepository.save(clearance));
    }

    public Optional<Clearance> getRequiredClearance(String requestUri, HttpMethod method) {
        return clearanceRepository.findByServletPathAndMethod(requestUri, method);
    }
}
