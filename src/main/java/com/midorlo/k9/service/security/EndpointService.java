package com.midorlo.k9.service.security;

import com.midorlo.k9.domain.security.RestResourceMetadata;
import com.midorlo.k9.repository.security.RestMetaRepository;
import org.springframework.stereotype.Service;

@Service
public class EndpointService {

    private final RestMetaRepository restMetaRepository;

    public EndpointService(RestMetaRepository restMetaRepository) {
        this.restMetaRepository = restMetaRepository;
    }

    public RestResourceMetadata createEndpointIfNotExists(RestResourceMetadata restResourceMetadata) {
        return restMetaRepository
                .findByServletPathEqualsIgnoreCase(restResourceMetadata.getServletPath())
                .orElse(restMetaRepository.save(restResourceMetadata));
    }
}
