package net.secudev.archi.service;

import java.util.List;

import net.secudev.archi.model.Task;

public interface ITaskService {
	
	Task findById(Long id) throws Exception;
	Task createTask(Task task) throws Exception;
	Task updateTask(Task task) throws Exception;	
	void removeTaskById(Long id) throws Exception;
	void removeAllColumnTasks(Long columnId) throws Exception;
	List<Task> getColumnTasks(Long columnId) throws Exception;
}
