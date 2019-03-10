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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import net.secudev.archi.dto.ProjectDTO;
import net.secudev.archi.dto.converter.ProjectDTOConverter;
import net.secudev.archi.infrastructure.Messages;
import net.secudev.archi.model.Project;
import net.secudev.archi.service.IProjectService;

@RestController
@RequestMapping("/api/v1/projects")
@Slf4j
public class ProjectController {

	@Autowired
	private IProjectService projectService;
	@Autowired
	private ProjectDTOConverter projectDTOConverter;

	@Autowired
	Messages messages;

	@PostMapping
	public ResponseEntity<Object> createProject(@RequestBody @Valid ProjectDTO projectDto) {
		try {
			// a voir, peut etre plutot le mettre à NULL que de renvoyer une erreur ou alors
			// faire cette manip ds le service
			if (projectDto.getId() != null)
				throw new Exception(
						"Project ID doit être null car il s'agit d'une creation (POST) et non d'un update (PUT)!");
			if (projectService.findByName(projectDto.getName()) != null)
				throw new Exception("Project Name deja utilisé!");

			Project result = projectService.createProject(projectDTOConverter.projectDtoToProject(projectDto));
			return new ResponseEntity<>(projectDTOConverter.projectToProjectDto(result), HttpStatus.CREATED);

		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping
	public ResponseEntity<Object> listAllProjects(@RequestParam(required = false, name = "kw") String keyword) {
		try {
			if (keyword != null && !keyword.isEmpty()) {
				return new ResponseEntity<>(
						projectDTOConverter.projectListToProjectDTOList(projectService.projectByKeyword(keyword)),
						HttpStatus.OK);
			}
			return new ResponseEntity<>(projectDTOConverter.projectListToProjectDTOList(projectService.allProject()),
					HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Object> findProjectById(@PathVariable Long id) {
		try {
			if (projectService.findById(id) == null)
				throw new Exception("Project Id inconnu!");
			return new ResponseEntity<>(projectDTOConverter.projectToProjectDto(projectService.findById(id)),
					HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Object> updateProject(@PathVariable Long id, @RequestBody @Valid ProjectDTO projectDto) {
		try {
			// on se fiche de l'id qui est ds le dto
			if (projectService.findById(id) == null)
				throw new Exception("Project Id inconnu!");
			projectDto.setId(id);
			Project result = projectService.updateProject(projectDTOConverter.projectDtoToProject(projectDto));
			return new ResponseEntity<>(projectDTOConverter.projectToProjectDto(result), HttpStatus.CREATED);
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteProjectById(@PathVariable Long id) {
		try {
			if (projectService.findById(id) == null)
				throw new Exception("Project Id inconnu!");

			projectService.removeProject(id);
			return new ResponseEntity<>("OK", HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping
	public ResponseEntity<Object> deleteAllProjects() {
		try {
			Long count = projectService.countProject();
			projectService.removeAllProjects();
			return new ResponseEntity<>("deleted " + count + " projects", HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}