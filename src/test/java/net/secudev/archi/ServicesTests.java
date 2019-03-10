package net.secudev.archi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import net.secudev.archi.model.Column;
import net.secudev.archi.model.Project;
import net.secudev.archi.model.Task;
import net.secudev.archi.service.impl.ColumnService;
import net.secudev.archi.service.impl.ProjectService;
import net.secudev.archi.service.impl.TaskService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServicesTests {

	@Autowired
	private ProjectService projectService;

	@Autowired
	private ColumnService columnService;

	@Autowired
	private TaskService taskService;
	
	@Test
	public void CRUDProject() throws Exception {

		// CReate and Retrive by name and by id
		Long id = projectService.createProject(new Project("java project")).getId();
		assertTrue(projectService.findByName("java project") != null);
		assertTrue(projectService.findById(id).getName().equals("java project"));
		assertTrue(projectService.allProject().size() == 1);

		// Autre forme de test ou la derniere fonction s 'adapte au type a evaluer, tres
		// riche
		assertThat(projectService.findById(id)).isNotNull();

		// update
		Project project = projectService.findById(id);
		project.setName("java project with spring");
		projectService.updateProject(project);
		assertTrue(projectService.findById(id).getName().equals("java project with spring"));
		assertTrue(projectService.allProject().size() == 1);

		// delete
		projectService.removeProject(id);
		assertTrue(projectService.allProject().size() == 0);
	}

	@Test
	public void CRUDColumn() throws Exception {
		// CReate and Retrive by name and by id
		Project project = projectService.createProject(new Project("java project"));
		Long id = columnService.createColumn(new Column("to do", project)).getId();
		assertTrue(projectService.findByName("java project").getColumns().size() == 1);
		assertTrue(columnService.getProjectColumns(project).get(0).getName().equals("to do"));
		assertTrue(columnService.findById(id).getName().equals("to do"));

		// Update
		Column toDo = (columnService.findById(id));
		toDo.setName("a faire");
		columnService.updateColumn(toDo);
		assertTrue(columnService.findById(id).getName().equals("a faire"));

		// Delete
		columnService.removeColumn(id);
		assertTrue(columnService.getProjectColumns(project.getId()).size() == 0);	
		projectService.removeProject(project.getId());
	}

	@Test
	public void CRUDTask() throws Exception {
		// CReate and Retrive by name and by id
		Project project = projectService.createProject(new Project("java project"));
		Column toDo = columnService.createColumn(new Column("to do", project));
		taskService.createTask(new Task("Modelize", toDo));
		assertTrue(columnService.findById(toDo.getId()).getTasks().size() == 1);
		assertTrue(projectService.findById(project.getId()).getColumns().get(0).getTasks().get(0).getName()
				.equals("Modelize"));

		// Update
		Task modelize = projectService.findById(project.getId()).getColumns().get(0).getTasks().get(0);
		modelize.setName("UML");
		taskService.updateTask(modelize);
		assertTrue(columnService.findById(toDo.getId()).getTasks().size() == 1);
		assertTrue(
				projectService.findById(project.getId()).getColumns().get(0).getTasks().get(0).getName().equals("UML"));

		// Delete
		taskService.removeTaskById(modelize.getId());
		assertTrue(columnService.findById(toDo.getId()).getTasks().size() == 1);
		projectService.removeProject(project.getId());
	}

}
