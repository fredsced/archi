package net.secudev.archi.api;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import net.secudev.archi.dto.TaskDTO;
import net.secudev.archi.dto.converter.TaskDTOConverter;
import net.secudev.archi.infrastructure.Messages;
import net.secudev.archi.model.Column;
import net.secudev.archi.model.Task;
import net.secudev.archi.service.IColumnService;
import net.secudev.archi.service.IProjectService;
import net.secudev.archi.service.impl.TaskService;

@RestController
@RequestMapping("/api/v1/projects")
@Slf4j
public class TaskController {

	@Autowired
	TaskService taskService;

	@Autowired
	private IProjectService projectService;

	@Autowired
	private IColumnService columnService;

	@Autowired
	TaskDTOConverter taskDtoConverter;

	@Autowired
	Messages messages;

	@PostMapping("/{projectId}/columns/{columnId}/tasks")
	public ResponseEntity<Object> createTask(@PathVariable Long projectId, @PathVariable Long columnId,
			@RequestBody @Valid TaskDTO taskDto) {
		try {
			// check projet
			if (projectService.findById(projectId) == null)
				throw new Exception(messages.get("PROJECT_ID_NOTFOUND") + projectId);

			// check column
			Column column = columnService.findById(columnId);
			if (column == null)
				throw new Exception(messages.get("COLUMN_ID_NOTFOUND") + columnId);

			// DTO to entity transformation
			Task task = taskDtoConverter.taskDtoToTask(taskDto);
			task.setColumn(column);

			// Saving
			task = taskService.createTask(task);

			// return DTO now
			return new ResponseEntity<>(taskDtoConverter.taskToTaskDto(task), HttpStatus.CREATED);

		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/{projectId}/columns/{columnId}/tasks")
	public ResponseEntity<Object> getAllTasks(@PathVariable Long projectId, @PathVariable Long columnId) {
		try {
			// check projet
			if (projectService.findById(projectId) == null)
				throw new Exception(messages.get("PROJECT_ID_NOTFOUND") + projectId);

			// check column
			Column column = columnService.findById(columnId);
			if (column == null)
				throw new Exception(messages.get("COLUMN_ID_NOTFOUND") + columnId);

			// return DTO List now
			return new ResponseEntity<>(taskDtoConverter.taskListToTaskDtoList(taskService.getColumnTasks(columnId)),
					HttpStatus.OK);

		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/{projectId}/columns/{columnId}/tasks/{taskId}")
	public ResponseEntity<Object> getTaskById(@PathVariable Long projectId, @PathVariable Long columnId,
			@PathVariable Long taskId) {
		try {
			// check task
			if (taskService.findById(taskId) == null)
				throw new Exception(messages.get("TASK_ID_NOTFOUND") + taskId);

			// check projet
			if (projectService.findById(projectId) == null)
				throw new Exception(messages.get("PROJECT_ID_NOTFOUND") + projectId);

			// check column
			Column column = columnService.findById(columnId);
			if (column == null)
				throw new Exception(messages.get("COLUMN_ID_NOTFOUND") + columnId);

			return new ResponseEntity<>(taskDtoConverter.taskToTaskDto(taskService.findById(taskId)), HttpStatus.OK);

		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/{projectId}/columns/{columnId}/tasks/{taskId}")
	public ResponseEntity<Object> updateTask(@PathVariable Long projectId, @PathVariable Long columnId,
			@PathVariable Long taskId, @RequestBody @Valid TaskDTO taskDto) {
		try {
			// check task
			if (taskService.findById(taskId) == null)
				throw new Exception(messages.get("TASK_ID_NOTFOUND") + taskId);

			// check projet
			if (projectService.findById(projectId) == null)
				throw new Exception(messages.get("PROJECT_ID_NOTFOUND") + projectId);

			// check column
			Column column = columnService.findById(columnId);
			if (column == null)
				throw new Exception(messages.get("COLUMN_ID_NOTFOUND") + columnId);

			// Updating
			taskDto.setId(taskId);
			taskDto.setColumnId(columnId);
			Task task = taskService.updateTask(taskDtoConverter.taskDtoToTask(taskDto));
			// return DTO now
			return new ResponseEntity<>(taskDtoConverter.taskToTaskDto(task), HttpStatus.OK);

		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/{projectId}/columns/{columnId}/tasks/{taskId}")
	public ResponseEntity<Object> deleteTaskById(@PathVariable Long projectId, @PathVariable Long columnId,
			@PathVariable Long taskId) {
		try {
			// check task
			if (taskService.findById(taskId) == null)
				throw new Exception(messages.get("TASK_ID_NOTFOUND") + taskId);

			// check projet
			if (projectService.findById(projectId) == null)
				throw new Exception(messages.get("PROJECT_ID_NOTFOUND") + projectId);

			// check column
			Column column = columnService.findById(columnId);
			if (column == null)
				throw new Exception(messages.get("COLUMN_ID_NOTFOUND") + columnId);

			taskService.removeTaskById(taskId);

			return new ResponseEntity<>("OK", HttpStatus.OK);

		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}