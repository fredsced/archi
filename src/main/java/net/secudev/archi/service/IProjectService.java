package net.secudev.archi.service;

import java.util.List;

import net.secudev.archi.model.Project;

public interface IProjectService {

	Project createProject(Project project) throws Exception;
	Project updateProject(Project project) throws Exception;
	List<Project> allProject();
	List<Project> projectByKeyword(String keyword);
	Project findById(Long id) throws Exception;
	Project findByName(String name);
	void removeProject(Long id);
	void removeAllProjects();
	Long countProject();
}
