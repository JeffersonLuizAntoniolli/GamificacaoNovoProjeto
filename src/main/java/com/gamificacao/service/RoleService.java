package com.gamificacao.service;

import org.springframework.beans.PropertyValues;

import com.gamificacao.model.Role;

import java.util.List;

public interface RoleService {
    Role createRole(Role role);

    List<Role> findAll();
}
