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
import net.secudev.archi.infrastructure.Messages;
import net.secudev.archi.model.Project;
import net.secudev.archi.repository.ProjectRepository;
import net.secudev.archi.service.IProjectService;
import net.secudev.archi.service.event.DomainEvent;
import net.secudev.archi.service.event.EventAction;
import net.secudev.archi.service.event.TargetEntity;

@Service
@Slf4j
public class ProjectService implements IProjectService {

	// UN peu peu plus et de meilleurs try catch plus des logs de trace et d info
	@Autowired
	private ProjectRepository projects;

	@Autowired Messages messages;
	
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private Validator validator = factory.getValidator();

	@Override
	@Transactional
	public Project createProject(@NotNull Project project) throws Exception {
		Project result = null;
		try {
			Set<ConstraintViolation<Project>> violations = validator.validate(project);
			List<String> errors = new ArrayList<>();
			for (ConstraintViolation<Project> violation : violations) {
				errors.add(violation.getMessage());
				log.error("Validation error : " + violation.getMessage());
			}
			if (!errors.isEmpty())
				throw new Exception(errors.toString());

			result = projects.save(project);

			applicationEventPublisher
					.publishEvent(new DomainEvent(this, result.getName(), TargetEntity.PROJECT, EventAction.CREATED));

		}

		catch (Exception e) {
			for (Throwable t = e.getCause(); t != null; t = t.getCause()) {
				log.error("Exception:" + t.getMessage());
			}
			throw e;
		}

		return result;
	}

	@Override
	public Project updateProject(@NotNull Project project) throws Exception {
		Project result = null;
		try {
			if (!projects.findById(project.getId()).isPresent())
				throw new Exception(messages.get("PROJECT_ID_NOTFOUND") + project.getId());
			
			Project oldProject = projects.findById(project.getId()).get();
			String oldname = oldProject.getName();
			
			Set<ConstraintViolation<Project>> violations = validator.validate(project);
			List<String> errors = new ArrayList<>();
			for (ConstraintViolation<Project> violation : violations) {
				errors.add(violation.getMessage());
				log.error("Validation error : " + violation.getMessage());
			}
			
			if (!errors.isEmpty())
				throw new Exception(errors.toString());

			// On va juste updater les proprités necessaires car sinon on va perdre les
			// colonnes, la liste columns va etre mise a NULL....
			oldProject.setName(project.getName());
			result = projects.save(oldProject);

			applicationEventPublisher.publishEvent(new DomainEvent(this, oldname + "->" + result.getName(),
					TargetEntity.PROJECT, EventAction.UPDATED));

		}

		catch (Exception e) {
			for (Throwable t = e.getCause(); t != null; t = t.getCause()) {
				log.error("Exception:" + t.getMessage());
			}
			throw e;
		}

		return result;
	}

	@Override
	public Long countProject() {
		return projects.count();
	}

	@Override
	public List<Project> allProject() {
		return projects.findAll();
	}

	@Override
	public List<Project> projectByKeyword(@NotNull String keyword) {
		return projects.findByNameContainsIgnoreCase(keyword);
	}

	@Override
	public Project findById(@NotNull Long id) throws Exception {		
			return projects.findById(id).orElseThrow(()->new Exception(messages.get("PROJECT_ID_NOTFOUND") +id));	
	}

	@Override
	@Transactional
	public void removeProject(@NotNull Long id) {

		try {
			String name = projects.findById(id).get().getName();
			projects.deleteById(id);
			applicationEventPublisher
					.publishEvent(new DomainEvent(this, name, TargetEntity.PROJECT, EventAction.DELETED));
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional
	public void removeAllProjects() {

		try {
			projects.deleteAll();
			applicationEventPublisher
					.publishEvent(new DomainEvent(this, "DELETE ALL", TargetEntity.PROJECT, EventAction.DELETED));
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Project findByName(@NotNull String name) {
		Project result = null;
		if (projects.findByName(name) != null)
			result = projects.findByName(name);
		return result;
	}

}
