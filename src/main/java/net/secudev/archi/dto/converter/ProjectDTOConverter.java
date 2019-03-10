package net.secudev.archi.dto.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.secudev.archi.dto.ProjectDTO;
import net.secudev.archi.model.Project;

@Component
public class ProjectDTOConverter {
	
	@Autowired
	ModelMapper modelMapper;
	
	public Project projectDtoToProject(ProjectDTO dto) {
		return modelMapper.map(dto, Project.class);
	}

	public ProjectDTO projectToProjectDto(Project entity) {	
		return modelMapper.map(entity, ProjectDTO.class);
	}

	public List<ProjectDTO> projectListToProjectDTOList(List<Project> projects) {
		return projects.stream().map(project -> projectToProjectDto(project)).collect(Collectors.toList());
	}
}
