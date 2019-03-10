package net.secudev.archi.dto.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.secudev.archi.dto.TaskDTO;
import net.secudev.archi.model.Task;

@Component
public class TaskDTOConverter {
	
	@Autowired
	ModelMapper modelMapper;
	
	public Task taskDtoToTask(TaskDTO dto) {		
		return modelMapper.map(dto, Task.class);
	}

	public TaskDTO taskToTaskDto(Task entity) {	
		return modelMapper.map(entity, TaskDTO.class);
	}
	public List<TaskDTO> taskListToTaskDtoList(List<Task> tasks) {
		return tasks.stream().map(task -> taskToTaskDto(task)).collect(Collectors.toList());
	}
}
