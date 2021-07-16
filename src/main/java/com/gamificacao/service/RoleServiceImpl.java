package com.gamificacao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gamificacao.model.Role;
import com.gamificacao.repository.RoleRepository;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override // Serviço que vai criar um novo papel no sistema
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    @Override // serviço que lista todos os Papeis no sistema
    public List<Role> findAll() {
        return roleRepository.findAll();
    }
}

