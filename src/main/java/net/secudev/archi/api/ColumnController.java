package net.secudev.archi.api;

import java.util.List;

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
import net.secudev.archi.dto.ColumnDTO;
import net.secudev.archi.dto.converter.ColumnDTOConverter;
import net.secudev.archi.infrastructure.Messages;
import net.secudev.archi.model.Column;
import net.secudev.archi.model.Project;
import net.secudev.archi.service.IColumnService;
import net.secudev.archi.service.IProjectService;

@RestController
@RequestMapping("/api/v1/projects")
@Slf4j
public class ColumnController {

	@Autowired
	private IProjectService projectService;

	@Autowired
	private IColumnService columnService;

	@Autowired
	private ColumnDTOConverter columnDTOConverter;

	@Autowired Messages messages;
//	private String projectIdNotFound;
//	private String columnIdNotFound;
//
//	public ColumnController(@Autowired Messages messages) {
//		projectIdNotFound = messages.getMessage("PROJECT_ID_NOTFOUND");
//		columnIdNotFound = messages.getMessage("COLUMN_ID_NOTFOUND");
//	}

	@PostMapping("/{projectId}/columns")
	public ResponseEntity<Object> createColumn(@PathVariable Long projectId, @RequestBody @Valid ColumnDTO columnDto) {

		try {
			// Checks
			// a voir, peut etre plutot le mettre à NULL que de renvoyer une erreur, ou
			// alors faire cette manip ds le service
			if (columnDto.getId() != null)
				throw new Exception(
						"Column ID doit être null car il s'agit d'une creation (POST) et non d'un update (PUT)!");
			Project project = projectService.findById(projectId);
			if (project == null)
				throw new Exception(messages.get("PROJECT_ID_NOTFOUND") + projectId);
			for (Column c : project.getColumns()) {
				if (c.getName().equals(columnDto.getName()))
					throw new Exception("Une colonne avec le même nom existe déjà : " + columnDto.getName());
			}

			// Action !!
			columnDto.setProjectId(projectId);
			Column column = columnService.createColumn(columnDTOConverter.columnDtoToColumn(columnDto));
			ColumnDTO result = columnDTOConverter.columnToColumnDto(column);

			return new ResponseEntity<>(result, HttpStatus.CREATED);

		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/{projectId}/columns")
	public ResponseEntity<Object> listColumnsByProject(@PathVariable Long projectId) {
		try {
			Project project = projectService.findById(projectId);
			if (project == null)
				throw new Exception(messages.get("PROJECT_ID_NOTFOUND") + projectId);
			List<ColumnDTO> result = columnDTOConverter
					.columnListToColumnDtoList(columnService.getProjectColumns(projectId));
			return new ResponseEntity<>(result, HttpStatus.CREATED);
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/{projectId}/columns/{id}")
	public ResponseEntity<Object> getColumnById(@PathVariable Long projectId, @PathVariable Long id) {
		try {
			Project project = projectService.findById(projectId);
			if (project == null)
				throw new Exception(messages.get("PROJECT_ID_NOTFOUND") + projectId);

			Column column = columnService.findById(id);
			if (column == null)
				throw new Exception(messages.get("COLUMN_ID_NOTFOUND") + id);
			
			if (!column.getProject().getId().equals(projectId))
				throw new Exception("cette colonne n'appartient pas au projet avec l'id " + projectId);
			return new ResponseEntity<>(columnDTOConverter.columnToColumnDto(columnService.findById(id)),
					HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/{projectId}/columns/{id}")
	public ResponseEntity<Object> updateColumn(@PathVariable Long projectId, @PathVariable Long id,
			@RequestBody @Valid ColumnDTO columnDto) {
		try {

			// Checks
			Column column = columnService.findById(id);
			if (column == null)
				throw new Exception(messages.get("COLUMN_ID_NOTFOUND") + id);

			if (column.getProject().getId().equals(projectId))
				throw new Exception("cette colonne n'appartient pas au projet avec l'id " + projectId);

			// Action !!
			columnDto.setId(id);
			columnDto.setProjectId(projectId);
			column = columnService.updateColumn(columnDTOConverter.columnDtoToColumn(columnDto));
			ColumnDTO result = columnDTOConverter.columnToColumnDto(column);

			return new ResponseEntity<>(result, HttpStatus.OK);

		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/{projectId}/columns/{id}")
	public ResponseEntity<Object> deleteColumn(@PathVariable Long id) {
		try {

			Column column = columnService.findById(id);
			if (column == null)
				throw new Exception(messages.get("COLUMN_ID_NOTFOUND") + id);

			columnService.removeColumn(id);
			return new ResponseEntity<>("OK", HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
