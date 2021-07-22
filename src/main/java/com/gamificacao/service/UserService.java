package com.gamificacao.service;

import java.util.List;

import com.gamificacao.model.Project;
import com.gamificacao.model.User;
import com.gamificacao.model.UserAffinity;
import com.gamificacao.model.UserItems;

public interface UserService {
    User createUser(User user); // Serviço para criar um novo usuário no sistema

    User changeRoleToAdmin(User user);  //Serviço para mudar o papel do usuario para Administrador do Sistema

    List<User> findAll(); // Serviço para listar todos os usuarios do sistema

    User getUserByEmail(String email);  //Serviço para Consultar usuário por seu Email

    boolean isUserEmailPresent(String email);  //Serviço para verificar se o email já existente em algum outro usuário no sistema

    User getUserById(Long userId);//Serviço para Consultar usuário pelo ID
 
    void deleteUser(Long id); //Serviço para Deletar Usuário do Sistema
    
    void updateUser(Long id, User user); //Serviço para atualizar o perfil Usuário do Sistema

	User changeRoleToGameMaster(User user);  //Serviço para mudar o papel do usuario para Game master do Sistema
   
	List<UserAffinity> ListAffinityByUser (Long id); // Serviço para buscar toda lista de afinidades que um determinado usuário possui
	
	List <UserItems> ListItemsByUser (Long id); // Serviço para buscar toda lista de Items que um determinado usuário possui
}
