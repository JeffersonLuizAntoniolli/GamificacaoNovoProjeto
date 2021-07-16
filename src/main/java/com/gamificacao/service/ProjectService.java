package com.gamificacao.service;

import java.util.List;

import com.gamificacao.model.Project;
import com.gamificacao.model.Task;

public interface ProjectService {

	    Project createProject(Project project);

	    void updateProject(Long id, Project project);

	    void deleteProject(Long id);
	    
	    List<Project> findAll();
	    
//	    List<Project> findByTaskOrderByDateDesc(Task task);
	    
	    void setProjectCompleted(Long id);
	    
	    void setProjectNotCompleted(Long id); 
	    
	    Project getProjectById(Long ProjectId); // serviço que busca um projeto pelo seu ID
}
