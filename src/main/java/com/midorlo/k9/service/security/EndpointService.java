package com.midorlo.k9.service.security;

import com.midorlo.k9.domain.core.ServletPath;
import com.midorlo.k9.domain.security.Authority;
import com.midorlo.k9.repository.security.AuthorityRepository;
import com.midorlo.k9.repository.security.RestMetaRepository;
import com.midorlo.k9.util.security.RestSecurityUtilities;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EndpointService {

    private final RestMetaRepository  restMetaRepository;
    private final AuthorityRepository authorityRepository;

    public EndpointService(RestMetaRepository restMetaRepository,
                           AuthorityRepository authorityRepository) {
        this.restMetaRepository  = restMetaRepository;
        this.authorityRepository = authorityRepository;
    }

    public ServletPath createEndpointIfNotExists(ServletPath servletPath) {
        return restMetaRepository
                .findByPath(servletPath.getPath())
                .orElse(restMetaRepository.save(servletPath));
    }


    public Map<ServletPath, Set<Authority>> generateServletPathWithDefaultAuthorities(String path) {
        ServletPath servletPath = restMetaRepository.save(new ServletPath(path));
        Set<Authority> collect = RestSecurityUtilities.ALL_METHODS.stream().map(m -> new Authority(m, servletPath))
                                                                   .map(authorityRepository::save)
                                                                   .collect(Collectors.toSet());
        return Map.of(servletPath, collect);
    }
}
