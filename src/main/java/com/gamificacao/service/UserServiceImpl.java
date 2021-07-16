package com.gamificacao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.gamificacao.model.Role;
import com.gamificacao.model.Task;
import com.gamificacao.model.User;
import com.gamificacao.repository.RoleRepository;
import com.gamificacao.repository.TaskRepository;
import com.gamificacao.repository.UserRepository;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private static final String ADMIN="ADMIN";
    private static final String USER="USER";
    
    private UserRepository userRepository;
    private TaskRepository taskRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           TaskRepository taskRepository,
                           RoleRepository roleRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override  // Serviço para criar um novo usuário no sistema
    public User createUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByRole(USER);
        user.setRoles(new ArrayList<>(Collections.singletonList(userRole)));
        return userRepository.save(user);
    }

    @Override //Serviço para mudar o papel do usuario para Administrador do Sistema
    public User changeRoleToAdmin(User user) {
        Role adminRole = roleRepository.findByRole(ADMIN);
        user.setRoles(new ArrayList<>(Collections.singletonList(adminRole)));
        return userRepository.save(user);
    }

    @Override // Serviço para listar todos os usuarios do sistema
    public List<User> findAll() { 
        return userRepository.findAll();
    }

    @Override  //Serviço para Consultar usuário por seu Email
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override //Serviço para verificar se o email já existente em algum outro usuário no sistema
    public boolean isUserEmailPresent(String email) { 
        return userRepository.findByEmail(email) != null;
    }

    @Override  //Serviço para Consultar usuário pelo ID
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override //Serviço para Deletar Usuário do Sistema
    public void deleteUser(Long id) {
        User user = userRepository.getOne(id);
        user.getTasksOwned().forEach(task -> task.setOwner(null));
        userRepository.delete(user);
    }

}

