package com.gamificacao.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gamificacao.model.Task;
import com.gamificacao.model.User;
import com.gamificacao.model.UserAffinity;
import com.gamificacao.repository.TaskRepository;
import com.gamificacao.repository.UserAffinityRepository;
import com.gamificacao.repository.UserRepository;

@Service
public class TaskServiceImpl implements TaskService {
    private TaskRepository taskRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserAffinityRepository userAffinityRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override // serviço para criar uma nova atividade no sistema
    public void createTask(Task task) {
        taskRepository.save(task);
    }

    @Override  // serviço para editar dados de uma atividade já cadastrada sistema
    public void updateTask(Long id, Task updatedTask) {
        Task task = taskRepository.getOne(id);
        task.setName(updatedTask.getName());
        task.setDescription(updatedTask.getDescription());
        task.setDate(updatedTask.getDate());
        task.setAffinity(updatedTask.getAffinity());
        taskRepository.save(task);
    }

    @Override // serviço para deletar uma atividade no sistema
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Override // serviço para listar todas as atividades do sistema
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override // Lista de atividades que busca pelo usuário e lista em data no formato descrecente
    public List<Task> findByOwnerOrderByDateDesc(User user) {
        return taskRepository.findByOwnerOrderByDateDesc(user);
    }

    @Override  // serviço que vai marcar uma atividade como concluida
    public void setTaskCompleted(Long id) {
        Task task = taskRepository.getOne(id);
        task.setCompleted(true);
        taskRepository.save(task);
        User user = task.getOwner();
        if(user != null && user.getId()!=null) {
        	if(user.getExperience()!=null) {
        		user.setExperience(user.getExperience()+1);
        	}else {
        		user.setExperience(1);
        	}
        	userRepository.save(user);
        }
        if(task.getAffinity()!=null) { //  vai atribuir ao usuário incrementa-la
        	UserAffinity userAffinity = 
        				userAffinityRepository.findByForeignKeysId(
        											task.getAffinity().getId(), 
        											user.getId()); 
        	if(userAffinity == null) { // se afinidade for nula, vai criar afinidade e da exp
        		userAffinity = new UserAffinity();
        		userAffinity.setAffinity(task.getAffinity());
        		userAffinity.setUser(user);
        		userAffinity.setExperience(1);
        	}else {
        		userAffinity.setExperience(userAffinity.getExperience()+1); // senão, vai aumentar afinidade que já existe
        	}
    		userAffinityRepository.save(userAffinity);
        }
        
    }

    @Override  // serviço que vai desmarcar uma atividade que estava concluida para ser concluida novamente
    public void setTaskNotCompleted(Long id) {
        Task task = taskRepository.getOne(id);
        task.setCompleted(false);
        taskRepository.save(task);
        User user = task.getOwner();
        
        if(user != null && user.getId()!=null) {
        	if(user.getExperience()!=null && user.getExperience() > 0) {
        		user.setExperience(user.getExperience()-1);
        	}else {
        		user.setExperience(0);
        	}
        	userRepository.save(user);
        }
        
        if(task.getAffinity()!=null) { //  vai atribuir ao usuário incrementa-la
        	UserAffinity userAffinity = 
        				userAffinityRepository.findByForeignKeysId(
        											task.getAffinity().getId(), 
        											user.getId()); 
        	if(userAffinity != null) { // se desmarcada atividade, vai ser retirado a experiencia da afinidade
        		userAffinity.setExperience(userAffinity.getExperience()-1);
        		userAffinityRepository.save(userAffinity);
        	} 		
        }
    }

    @Override // serviço que lista todas atividades sem um usuário responsavel para ser encaminhado
    public List<Task> findFreeTasks() {
        return taskRepository.findAll()
                .stream()
                .filter(task -> task.getOwner() == null && !task.isCompleted())
                .collect(Collectors.toList());

    }

    @Override  // serviço que busca uma atividade pelo seu ID
    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    @Override  // serviço que faz a função de encaminhar a atividade para um usuario no sistema
    public void assignTaskToUser(Task task, User user) {
        task.setOwner(user);
        taskRepository.save(task);
    }

    @Override // serviço que faz a função de retirar o responsavel da atividade para possa ser encaminhada para outro usuário 
    public void unassignTask(Task task) {
        task.setOwner(null);
        taskRepository.save(task);
    }

}
