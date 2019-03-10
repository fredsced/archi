package net.secudev.archi.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.secudev.archi.model.Column;
import net.secudev.archi.model.Task;
import net.secudev.archi.repository.ColumnRepository;
import net.secudev.archi.repository.TaskRepository;
import net.secudev.archi.service.ITaskService;
import net.secudev.archi.service.event.DomainEvent;
import net.secudev.archi.service.event.EventAction;
import net.secudev.archi.service.event.TargetEntity;

@Service
@Slf4j
public class TaskService implements ITaskService {

	@Autowired
	TaskRepository tasks;

	@Autowired
	ColumnRepository columns;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private Validator validator = factory.getValidator();

	@Override
	public Task findById(@NotNull Long id) throws Exception {
		if (!tasks.findById(id).isPresent())
			throw new Exception("No task with id " + id);
		return tasks.findById(id).get();
	}

	@Override
	@Transactional
	public Task createTask(@NotNull Task task) throws Exception {
		Task result = null;
		try {
			Set<ConstraintViolation<Task>> violations = validator.validate(task);
			List<String> errors = new ArrayList<>();
			for (ConstraintViolation<Task> violation : violations) {
				errors.add(violation.getMessage());
				log.error("Validation error : " + violation.getMessage());
			}
			if (!errors.isEmpty())
				throw new Exception(errors.toString());

			result = tasks.save(task);
			applicationEventPublisher
					.publishEvent(new DomainEvent(this, result.getName(), TargetEntity.TASK, EventAction.CREATED));

		} catch (Exception e) {
			for (Throwable t = e.getCause(); t != null; t = t.getCause()) {
				log.error(t.getMessage());
			}
			throw e;
		}
		return task;
	}

	@Override
	@Transactional
	public Task updateTask(@NotNull Task task) throws Exception {
		Task result = null;
		try {
			// Update, on verifie donc si l'entity existe
			if (!tasks.findById(task.getId()).isPresent())
				throw new Exception("No tasks with id " + task.getId());

			// Recupération de l'ancienne / actuelle entity pour comparer plus tard les
			// changements
			Task oldTask = tasks.findById(task.getId()).get();
			String oldName = oldTask.getName();
			int oldOrder = oldTask.getOrder();
			int oldPriority = oldTask.getPriority();
			String oldColumn = oldTask.getColumn().getName();
			// Etc si d'autre props

			// verification des contraintes de Validation
			Set<ConstraintViolation<Task>> violations = validator.validate(task);
			List<String> errors = new ArrayList<>();
			for (ConstraintViolation<Task> violation : violations) {
				errors.add(violation.getMessage());
				log.error("Validation error : " + violation.getMessage());
			}
			if (!errors.isEmpty())
				throw new Exception(errors.toString());

			// Persitence JPA
			result = tasks.save(task);

			// Construction du message d'EVENT
			StringBuilder sb = new StringBuilder();
			sb.append(oldName + " -> " + result.getName() + ", ");
			sb.append(oldOrder + " -> " + result.getOrder() + ", ");
			sb.append(oldPriority + " -> " + result.getPriority() + ", ");
			sb.append(oldColumn + " -> " + result.getColumn().getName());
			String changes = sb.toString();

			applicationEventPublisher
					.publishEvent(new DomainEvent(this, changes, TargetEntity.TASK, EventAction.UPDATED));

		} catch (Exception e) {
			for (Throwable t = e.getCause(); t != null; t = t.getCause()) {
				log.error(t.getMessage());
			}
			throw e;
		}
		return task;
	}

	@Override
	@Transactional
	public void removeAllColumnTasks(@NotNull Long columnId) throws Exception {
		if (!columns.findById(columnId).isPresent())
			throw new Exception("No column with id " + columnId);
		try {
			Column target = columns.findById(columnId).get();
			target.removeAllTasks();
			columns.save(target);
			applicationEventPublisher.publishEvent(
					new DomainEvent(this, "ALL tasks from " + target.getProject().getName() + "\\" + target.getName(),
							TargetEntity.TASK, EventAction.DELETED));

		} catch (Exception e) {
			for (Throwable t = e.getCause(); t != null; t = t.getCause()) {
				log.error(t.getMessage());
			}
			throw e;
		}
	}

	@Override
	@Transactional
	public void removeTaskById(Long id) throws Exception {
		if (!tasks.findById(id).isPresent())
			throw new Exception("No tasks with id " + id);
	}

	@Override
	public List<Task> getColumnTasks(Long columnId) throws Exception {
		try {
			if (!columns.findById(columnId).isPresent())
				throw new Exception("No column with id " + columnId);
			Column target = columns.findById(columnId).get();
			return target.getTasks();

		} catch (Exception e) {
			for (Throwable t = e.getCause(); t != null; t = t.getCause()) {
				log.error(t.getMessage());
			}
			throw e;
		}
	}
}