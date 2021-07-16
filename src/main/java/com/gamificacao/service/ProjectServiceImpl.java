package com.gamificacao.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gamificacao.model.Project;
import com.gamificacao.model.Task;
import com.gamificacao.repository.ProjectRepository;
import com.gamificacao.repository.TaskRepository;

@Service
public class ProjectServiceImpl implements ProjectService{
	private ProjectRepository projectRepository;
	
	@Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }
	
	@Override
	public Project createProject(Project project) {
		return projectRepository.save(project);
		
	}

	@Override
	public void updateProject(Long id, Project updatedProject) {
		  	Project project = projectRepository.getOne(id);
	        project.setName(updatedProject.getName());
	        project.setDate(updatedProject.getDate());
	        projectRepository.save(project);
	}

	@Override
	public void deleteProject(Long id) {
		projectRepository.deleteById(id);
	}

	@Override
	public List<Project> findAll() {
		return projectRepository.findAll();
	}

	@Override
	public void setProjectCompleted(Long id) {
		Project project = projectRepository.getOne(id);
        project.setCompleted(true);
        projectRepository.save(project);
		
	}

	@Override
	public void setProjectNotCompleted(Long id) {
		  Project project = projectRepository.getOne(id);
	        project.setCompleted(false);
	        projectRepository.save(project);
	}

	@Override
	public Project getProjectById(Long id) {
		return projectRepository.findById(id).orElse(null);
	}
	
}
