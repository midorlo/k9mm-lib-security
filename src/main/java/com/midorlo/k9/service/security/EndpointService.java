package com.midorlo.k9.service.security;

import com.midorlo.k9.domain.core.ServletDescription;
import com.midorlo.k9.repository.security.AuthorityRepository;
import com.midorlo.k9.repository.security.RestMetaRepository;
import org.springframework.stereotype.Service;

@Service
public class EndpointService {

    private final RestMetaRepository  restMetaRepository;
    private final AuthorityRepository authorityRepository;

    public EndpointService(RestMetaRepository restMetaRepository,
                           AuthorityRepository authorityRepository) {
        this.restMetaRepository  = restMetaRepository;
        this.authorityRepository = authorityRepository;
    }

    public ServletDescription createEndpointIfNotExists(ServletDescription servletDescription) {
        return restMetaRepository
                .findByPath(servletDescription.getPath())
                .orElse(restMetaRepository.save(servletDescription));
    }


//    public Map<ServletDescription, Set<Authority>> generateServletPathWithDefaultAuthorities(String path) {
//        ServletDescription servletDescription = restMetaRepository.save(new ServletDescription(path));
//        Set<Authority> collect = RestSecurityUtilities.ALL_METHODS.stream().map(m -> new Authority(m,
//        servletDescription))
//                                                                   .map(authorityRepository::save)
//                                                                   .collect(Collectors.toSet());
//        return Map.of(servletDescription, collect);
//    }
}
