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

import com.gamificacao.model.Project;
import com.gamificacao.model.Task;
import com.gamificacao.model.User;
import com.gamificacao.service.ProjectService;
import com.gamificacao.service.TaskService;
import com.gamificacao.service.UserService;

@Controller
public class ProjectController {

	 private ProjectService projectService;
	 private TaskService taskService;
	 private UserService userService;
	 
	 @Autowired
	 public ProjectController(ProjectService projectService, TaskService taskService, UserService userService) {
	        this.taskService = taskService;
	        this.userService = userService;
	        this.projectService = projectService;
	    }
	
	 @GetMapping("/projects")
	 public String listTasks(Model model, Principal principal, SecurityContextHolderAwareRequestWrapper request) {
	       	prepareProjectsListModel(model, principal, request);
	       	model.addAttribute("onlyInProgress", false);
	        return "views/projects";
	    }

	private void prepareProjectsListModel(Model model, Principal principal,
			SecurityContextHolderAwareRequestWrapper request) {
		 	
	       boolean isAdminSigned = request.isUserInRole("ROLE_ADMIN");

	        model.addAttribute("projects", projectService.findAll());
	       // model.addAttribute("users", userService.findAll());
	     //  model.addAttribute("signedUser", principal);
	       model.addAttribute("isAdminSigned", isAdminSigned);
	}
	 
	@GetMapping("/project/mark-done/{id}")
    public String setprojectCompleted(@PathVariable Long id) {
        	projectService.setProjectCompleted(id);
        	return "redirect:/projects";
    }

    @GetMapping("/project/unmark-done/{id}")
    public String setProjectNotCompleted(@PathVariable Long id) {
        	projectService.setProjectNotCompleted(id);
        	return "redirect:/projects";
    }
    
    @GetMapping("/project/delete/{id}")
    public String deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return "redirect:/projects";
    }
    
 // Vai apresentar a pagina de edição do projeto
    @GetMapping("/project/edit/{id}")
    public String showFilledProjectForm(@PathVariable Long id, Model model) {
        model.addAttribute("project", projectService.getProjectById(id));
        return "forms/project-edit";
    }
 // Vai salvar os dados alterados e voltar a pagina de listagem de projetos
    @PostMapping("/project/edit/{id}")
    public String updateProject(@Valid Project project, BindingResult bindingResult, @PathVariable Long id, Model model) {
        if (bindingResult.hasErrors()) {
            return "forms/project-edit";
        }
        projectService.updateProject(id, project);
        return "redirect:/projects";
    }
    
    //pagina para criar um novo projeto
    @GetMapping("/project/create")
    public String showEmptyProjectForm(Model model, Principal principal, SecurityContextHolderAwareRequestWrapper request) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email);

        Project project = new Project();
        project.setCreatorName(user.getName());
        model.addAttribute("project", project);
        return "forms/project-new";
    }
}
