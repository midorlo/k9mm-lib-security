package com.midorlo.k9.service.security;

import com.midorlo.k9.domain.security.RestMeta;
import com.midorlo.k9.repository.security.RestMetaRepository;
import org.springframework.stereotype.Service;

@Service
public class EndpointService {

    private final RestMetaRepository restMetaRepository;

    public EndpointService(RestMetaRepository restMetaRepository) {
        this.restMetaRepository = restMetaRepository;
    }

    public RestMeta createEndpointIfNotExists(RestMeta restMeta) {
        return restMetaRepository
                .findByServletPathEqualsIgnoreCase(restMeta.getServletPath())
                .orElse(restMetaRepository.save(restMeta));
    }
}
