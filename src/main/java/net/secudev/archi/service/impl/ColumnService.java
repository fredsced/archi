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
import net.secudev.archi.model.Project;
import net.secudev.archi.repository.ColumnRepository;
import net.secudev.archi.repository.ProjectRepository;
import net.secudev.archi.service.IColumnService;
import net.secudev.archi.service.event.DomainEvent;
import net.secudev.archi.service.event.EventAction;
import net.secudev.archi.service.event.TargetEntity;

@Service
@Slf4j
public class ColumnService implements IColumnService {

	@Autowired
	private ProjectRepository projects;

	@Autowired
	private ColumnRepository columns;
	
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private Validator validator = factory.getValidator();

	private static final String PROJECT_ID_NOTFOUND = "aucun projet avec l'id ";
	
	@Override
	@Transactional
	public Column createColumn(@NotNull Column column) throws Exception {

		Column result = null;

		try {
			if (!projects.findById(column.getProject().getId()).isPresent())
				throw new Exception(PROJECT_ID_NOTFOUND + column.getProject().getId());

			Set<ConstraintViolation<Column>> violations = validator.validate(column);
			List<String> errors = new ArrayList<>();
			for (ConstraintViolation<Column> violation : violations) {
				errors.add(violation.getMessage());
				log.error("Validation error : " + violation.getMessage());
			}
			if (!errors.isEmpty())
				throw new Exception(errors.toString());

			result = columns.save(column);
			Project project = projects.findById(column.getProject().getId()).get();
			project.addColumn(result);
			projects.save(project);
			

			applicationEventPublisher
					.publishEvent(new DomainEvent(this, result.getName(), TargetEntity.COLUMN, EventAction.CREATED));
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
	@Transactional
	public Column updateColumn(@NotNull Column column) throws Exception {
		Column result = null;

		try {
			if (!projects.findById(column.getProject().getId()).isPresent())
				throw new Exception(PROJECT_ID_NOTFOUND + column.getProject().getId());

			Column oldColumn = columns.findById(column.getId()).get();
			String oldName=oldColumn.getName();
			
			Set<ConstraintViolation<Column>> violations = validator.validate(column);
			List<String> errors = new ArrayList<>();
			for (ConstraintViolation<Column> violation : violations) {
				errors.add(violation.getMessage());
				log.error("Validation error : " + violation.getMessage());
			}
			if (!errors.isEmpty())
				throw new Exception(errors.toString());

			// On va juste updater les proprités necessaires car sinon on va perdre les
						// tasks, la liste tasks va etre mise a NULL....
			oldColumn.setName(column.getName());
						result = columns.save(oldColumn);

						applicationEventPublisher.publishEvent(new DomainEvent(this, oldName + "->" + result.getName(),
								TargetEntity.COLUMN, EventAction.UPDATED));
			
			result = columns.save(column);
			Project project = projects.findById(column.getProject().getId()).get();
			project.addColumn(result);
			projects.save(project);
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
	@Transactional
	public void removeColumn(@NotNull Column column) {
		columns.delete(column);
	}

	@Override
	public List<Column> getProjectColumns(@NotNull Project project) throws Exception {

		if (!projects.findById(project.getId()).isPresent())
			throw new Exception(PROJECT_ID_NOTFOUND + project.getId());
		return columns.findByProject(project);
	}
	
	@Override
	public List<Column> getProjectColumns(@NotNull Long projectId) throws Exception {
		if (!projects.findById(projectId).isPresent())
			throw new Exception(PROJECT_ID_NOTFOUND + projectId);
		return columns.findByProject(projects.findById(projectId).get());
	}	

	@Override
	public Column findById(@NotNull Long id) {
		if(columns.findById(id).isPresent()) return columns.findById(id).get();
		return null;
	}

	@Override
	@Transactional
	public void removeColumn(@NotNull Long id) {
		columns.deleteById(id);		
	}
}
