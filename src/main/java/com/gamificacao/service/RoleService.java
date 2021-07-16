package com.gamificacao.service;

import org.springframework.beans.PropertyValues;

import com.gamificacao.model.Role;

import java.util.List;

public interface RoleService {
    Role createRole(Role role);  // Serviço que vai criar um novo papel no sistema

    List<Role> findAll(); // serviço que lista todos os Papeis no sistema
}
