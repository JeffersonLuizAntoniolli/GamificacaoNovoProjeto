package com.gamificacao.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
	 
	 
}
