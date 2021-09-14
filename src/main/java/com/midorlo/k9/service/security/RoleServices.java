package com.midorlo.k9.service.security;

import com.midorlo.k9.domain.security.Role;
import com.midorlo.k9.repository.security.RoleRepository;
import org.springframework.stereotype.Service;

@SuppressWarnings("unused")
@Service
public class RoleServices {

    private final RoleRepository roleRepository;

    public RoleServices(RoleRepository roleRepository) {this.roleRepository = roleRepository;}

    public Role createIfNotExists(String name) {
        return roleRepository.findByName(name).orElse(roleRepository.save(new Role(name)));
    }
}
