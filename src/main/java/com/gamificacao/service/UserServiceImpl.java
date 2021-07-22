package com.gamificacao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.gamificacao.model.Project;
import com.gamificacao.model.Role;
import com.gamificacao.model.Task;
import com.gamificacao.model.User;
import com.gamificacao.model.UserAffinity;
import com.gamificacao.model.UserItems;
import com.gamificacao.repository.RoleRepository;
import com.gamificacao.repository.TaskRepository;
import com.gamificacao.repository.UserAffinityRepository;
import com.gamificacao.repository.UserItemRepository;
import com.gamificacao.repository.UserRepository;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private static final String ADMIN="ADMIN";
    private static final String USER="USER";
    private static final String GAMEMASTER="GAMEMASTER";
    private UserRepository userRepository;
    private TaskRepository taskRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserAffinityRepository userAffinityRepository;
    @Autowired
    private UserItemRepository userItemRepository;
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
    	user.setExperience(0);
    	user.setPoints(new Long(0));
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

	@Override // Serviço que vai fazer atualização das informações basicas de perfil do usuario
	public void updateUser(Long id, User updatedUser) {
		User user = userRepository.getOne(id);
        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
     //   user.setPassword(updatedUser.getPassword());
    //    user.setPhoto(updatedUser.getPhoto());
        
        userRepository.save(user);
	}
	
	@Override //Serviço para mudar o papel do usuario para Gamemaster do Sistema
    public User changeRoleToGameMaster(User user) {
        Role gameMasterRole = roleRepository.findByRole(GAMEMASTER);
        user.setRoles(new ArrayList<>(Collections.singletonList(gameMasterRole)));
        return userRepository.save(user);
    }

	@Override // serviço que vai listar todas afinidades do usuário;
	public List<UserAffinity> ListAffinityByUser(Long id) {
		return userAffinityRepository.ListAffinityByUser(id);
	}

	
	  @Override // serviço que vai listar todos itens do usuário; public
	 public List<UserItems> ListItemsByUser(Long id) { 
		  return userItemRepository.ListItemsByUser(id); }
	 
}

