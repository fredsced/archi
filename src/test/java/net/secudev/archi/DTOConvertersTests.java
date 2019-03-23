package net.secudev.archi;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import net.secudev.archi.dto.ColumnDTO;
import net.secudev.archi.dto.TaskDTO;
import net.secudev.archi.dto.converter.ColumnDTOConverter;
import net.secudev.archi.dto.converter.TaskDTOConverter;
import net.secudev.archi.model.Column;
import net.secudev.archi.model.Project;
import net.secudev.archi.model.Task;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DTOConvertersTests {

	@Autowired
	ColumnDTOConverter columnDTOConverter;
	
	@Autowired
	TaskDTOConverter taskDTOConverter;

	@Test
	public void testColumnConverter() {
		// Entity to DTO		
		Project someProject = new Project("kanban2");
		someProject.setId(5L);
		Column col = new Column("to do",someProject);
		col.setId(1L);
		ColumnDTO dto = columnDTOConverter.columnToColumnDto(col);
		assertTrue(dto.getName().equals("to do"));
		assertTrue(dto.getId() == 1L);
		assertTrue(dto.getProjectName().equals("kanban2"));
		assertTrue(dto.getProjectId() == 5L);

		// DTO to entity
		ColumnDTO colDto = new ColumnDTO();
		colDto.setName("to do");
		colDto.setId(1L);
		assertTrue(columnDTOConverter.columnDtoToColumn(colDto).getName().equals("to do"));
		assertTrue(columnDTOConverter.columnDtoToColumn(colDto).getId() == 1L);
	}
	
	@Test
	public void nullValues()
	{
		Project project = new Project("kanban");
		project.setId(1L);
		Column column = new Column("to do", project);
		column.setId(2L);
		Task task = new Task("model map",column);
		task.setId(3L);
		
		TaskDTO dto = new TaskDTO();
		dto.setName("to do 2");
		
		//copy de valeurs non nulles du dto vers l'entité
		taskDTOConverter.taskDtoToTaskCopy(dto, task);
		//on check qu'elles n'ont pas bougé
		assertTrue(task.getName().equals("to do 2"));
		assertTrue(task.getColumn().equals(column));
		assertTrue(task.getId().equals(3L))	;
		
	}

}
