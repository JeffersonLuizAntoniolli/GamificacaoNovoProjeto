package com.gamificacao.service;

import java.util.List;

import com.gamificacao.model.Task;
import com.gamificacao.model.User;

public interface TaskService {

    void createTask(Task task); // serviço para criar uma nova atividade no sistema
 
    void updateTask(Long id, Task task); // serviço para editar dados de uma atividade já cadastrada sistema

    void deleteTask(Long id);  // serviço para deletar uma atividade no sistema

    List<Task> findAll();  // serviço para listar todas as atividades do sistema

    List<Task> findByOwnerOrderByDateDesc(User user); // Lista de atividades que busca pelo usuário e lista em data no formato descrecente

    void setTaskCompleted(Long id); // serviço que vai marcar uma atividade como concluida

    void setTaskNotCompleted(Long id);  // serviço que vai desmarcar uma atividade que estava concluida para ser concluida novamente

    List<Task> findFreeTasks(); // serviço que lista todas atividades sem um usuário responsavel para ser encaminhado

    Task getTaskById(Long taskId); // serviço que busca uma atividade pelo seu ID

    void assignTaskToUser(Task task, User user); // serviço que faz a função de encaminhar a atividade para um usuario no sistema

    void unassignTask(Task task); // serviço que faz a função de retirar o responsavel da atividade para possa ser encaminhada para outro usuário 
}
