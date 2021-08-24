package com.midorlo.k9.service.security;

import com.midorlo.k9.domain.security.Role;
import com.midorlo.k9.repository.security.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {this.roleRepository = roleRepository;}

    public Role createIfNotExists(String name) {
        return roleRepository.findByName(name).orElse(roleRepository.save(new Role(name)));
    }
}
