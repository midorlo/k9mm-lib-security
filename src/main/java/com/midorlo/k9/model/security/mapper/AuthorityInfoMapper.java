package com.midorlo.k9.model.security.mapper;

import com.midorlo.k9.domain.security.Authority;
import org.springframework.http.HttpMethod;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AuthorityInfoMapper {
    public Map<String, Map<HttpMethod, Long>> toTree(Collection<Authority> authorityInfos) {
        Map<String, Map<HttpMethod, Long>> tree = new HashMap<>();
        for (Authority authority : authorityInfos) {
            Long       id          = authority.getId();
            String     servletPath = authority.getServletDescription().getPath();
            HttpMethod method      = authority.getMethod();
            if (!tree.containsKey(servletPath)) {
                tree.put(servletPath, new HashMap<>());
            }
            Map<HttpMethod, Long> methodMap = tree.get(servletPath);
            methodMap.put(method, id);
        }
        return tree;
    }
}
