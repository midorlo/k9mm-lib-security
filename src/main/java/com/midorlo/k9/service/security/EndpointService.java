package com.midorlo.k9.service.security;

import com.midorlo.k9.domain.security.Endpoint;
import com.midorlo.k9.repository.security.EndpointRepository;
import org.springframework.stereotype.Service;

@Service
public class EndpointService {

    private final EndpointRepository endpointRepository;

    public EndpointService(EndpointRepository endpointRepository) {
        this.endpointRepository = endpointRepository;
    }

    public Endpoint createEndpointIfNotExists(Endpoint endpoint) {
        return endpointRepository
                .findByServletPathEqualsIgnoreCase(endpoint.getServletPath())
                .orElse(endpointRepository.save(endpoint));
    }
}
