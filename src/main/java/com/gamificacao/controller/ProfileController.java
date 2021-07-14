package com.gamificacao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.gamificacao.model.User;
import com.gamificacao.service.TaskService;
import com.gamificacao.service.UserService;

import java.security.Principal;

@Controller
public class ProfileController {

    private UserService userService;
    private TaskService taskService;

    @Autowired
    public ProfileController(UserService userService, TaskService taskService) {
        this.userService = userService;
        this.taskService = taskService;
    }
    // Metodo que vai direcionar para página do perfil, então também consultar seus dados, como também listar suas atividades
    @GetMapping("/profile")
    public String showProfilePage(Model model, Principal principal) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email);
        model.addAttribute("user", user);
        model.addAttribute("tasksOwned", taskService.findByOwnerOrderByDateDesc(user));
        return "views/profile";
    }
    // na pagina do perfil onde vai listar as atividades do usuário como responsavel, metodo que vai marcar como completo 
    @GetMapping("/profile/mark-done/{taskId}")
    public String setTaskCompleted(@PathVariable Long taskId) {
        taskService.setTaskCompleted(taskId);
        return "redirect:/profile";
    }
    // na pagina do perfil onde vai listar as atividades do usuário como responsavel, metodo que vai marcar para ser feito novamente 
    @GetMapping("/profile/unmark-done/{taskId}")
    public String setTaskNotCompleted(@PathVariable Long taskId) {
        taskService.setTaskNotCompleted(taskId);
        return "redirect:/profile";
    }

}
