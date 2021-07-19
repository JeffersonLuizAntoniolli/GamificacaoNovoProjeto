package com.gamificacao.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.gamificacao.model.Affinity;
import com.gamificacao.model.Task;
import com.gamificacao.model.User;
import com.gamificacao.service.AffinityService;
import com.gamificacao.service.TaskService;
import com.gamificacao.service.UserService;

@Controller
public class TaskController {

    private TaskService taskService;
    private UserService userService;
    @Autowired // instancia no metodo construtor para o objetivo não ficar nulo e não precisar colocar manualmente
    private AffinityService affinityService;

    @Autowired
    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping("/tasks")
    public String listTasks(Model model, Principal principal, SecurityContextHolderAwareRequestWrapper request) {
        prepareTasksListModel(model, principal, request);
        model.addAttribute("onlyInProgress", false);
        return "views/tasks";
    }

    @GetMapping("/tasks/in-progress")
    public String listTasksInProgress(Model model, Principal principal, SecurityContextHolderAwareRequestWrapper request) {
        prepareTasksListModel(model, principal, request);
        model.addAttribute("onlyInProgress", true);
        return "views/tasks";
    }

    private void prepareTasksListModel(Model model, Principal principal, SecurityContextHolderAwareRequestWrapper request) {
        String email = principal.getName();
        User signedUser = userService.getUserByEmail(email);
        boolean isAdminSigned = request.isUserInRole("ROLE_ADMIN");

        model.addAttribute("tasks", taskService.findAll());
        model.addAttribute("users", userService.findAll());
        model.addAttribute("signedUser", signedUser);
        model.addAttribute("isAdminSigned", isAdminSigned);

    }

    //pagina para criar uma nova atividade
    @GetMapping("/task/create")
    public String showEmptyTaskForm(Model model, Principal principal, SecurityContextHolderAwareRequestWrapper request) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email);
        model.addAttribute("affinitys", affinityService.findAll());
        Task task = new Task();
        task.setCreatorName(user.getName());
        if (request.isUserInRole("ROLE_USER")) { // Se o Usuário for o proprio colaborador, atividade vai designar ele mesmo como responsavel pela atividade criada
            task.setOwner(user); 	// Caso a atividade não seja, será criado atividade sem um responsavel, nesse o Admin encaminha a atividade para um colaborador
        }
        model.addAttribute("task", task);
        return "forms/task-new";
    }
    
    
    //pagina para criar uma nova atividade
    @PostMapping("/task/create")
    public String createTask(@Valid Task task, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "forms/task-new";
        }
        taskService.createTask(task);
        return "redirect:/project-tasks/"+task.getProject().getId();
    }
    // Vai apresentar a pagina de edição da atividade
    @GetMapping("/task/edit/{id}")
    public String showFilledTaskForm(@PathVariable Long id, Model model) {
        model.addAttribute("task", taskService.getTaskById(id));
        return "forms/task-edit";
    }
 // Vai salvar os dados alterados e voltar a pagina de listagem da atividade
    @PostMapping("/task/edit/{id}")
    public String updateTask(@Valid Task task, BindingResult bindingResult, @PathVariable Long id, Model model) {
        if (bindingResult.hasErrors()) {
            return "forms/task-edit";
        }
        taskService.updateTask(id, task);
        task = taskService.getTaskById(id);
        return "redirect:/project-tasks/"+task.getProject().getId();
    }

    @GetMapping("/task/delete/{id}")
    public String deleteTask(@PathVariable Long id) {     
        Task task = taskService.getTaskById(id);
        taskService.deleteTask(id);
        return "redirect:/project-tasks/"+task.getProject().getId();
    }

    @GetMapping("/task/mark-done/{id}")
    public String setTaskCompleted(@PathVariable Long id) {
        taskService.setTaskCompleted(id);
        Task task = taskService.getTaskById(id);
        return "redirect:/project-tasks/"+task.getProject().getId();
    }

    @GetMapping("/task/unmark-done/{id}")
    public String setTaskNotCompleted(@PathVariable Long id) {
        taskService.setTaskNotCompleted(id);
        Task task = taskService.getTaskById(id);
        return "redirect:/project-tasks/"+task.getProject().getId();
    }

}
