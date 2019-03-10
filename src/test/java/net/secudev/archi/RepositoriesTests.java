package net.secudev.archi;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import net.secudev.archi.model.Column;
import net.secudev.archi.model.Project;
import net.secudev.archi.model.Task;
import net.secudev.archi.repository.ColumnRepository;
import net.secudev.archi.repository.ProjectRepository;
import net.secudev.archi.repository.TaskRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RepositoriesTests {

	//Je ne vais pas tester tt les methodes des repos de spring car elles sont déja bien testees
	//Neanmoin sje vais valider que mes spring data query dans mes repos font bien ce que je souhaite
	//Et au passage c'est ici aussi que je vais tester mes contraintes de validation pour voir si elles levent bien des exceptions
	//Et pour finir je teste un petit scenario qui met en jeu mes repos
	
	@Autowired
	private ProjectRepository projects;

	@Autowired
	private ColumnRepository columns;

	@Autowired
	private TaskRepository tasks;

	@Test
	public void testProject() {
		//findByName;
		//findByNameContainingIgnoreCase	
		
	    projects.save(new Project("Kata"));
	    projects.save(new Project("Kata2"));
	    
	    assertNotNull(projects.findByName("Kata"));
	    assertTrue(projects.findByName("kata")==null);
	    assertTrue(projects.findByNameContainingIgnoreCase("ka").size()==2);
	    assertTrue(projects.findByNameContainingIgnoreCase("aka").size()==0);		
	    
	    projects.deleteAll();
	}

	@Test
	public void testColumn() {
		//findByProject
		 Project kata = projects.save(new Project("Kata"));
		 kata.addColumn(new Column("to do"));
		 kata.addColumn(new Column("in progress"));
		 projects.save(kata);
		 
		 assertTrue(columns.findByProject(kata).size()==2);		
		 
		 projects.deleteAll();
	}

	@Test
	public void testTask() {
		//findByColumn
		 Project kata = projects.save(new Project("Kata"));
		 kata.addColumn(new Column("to do"));
		 kata.addColumn(new Column("in progress"));		
		 projects.save(kata);
		 
		 Column toDo = columns.findByProject(kata).get(0);
		 toDo.addTask(new Task("do this"));
		 toDo.addTask(new Task("do that"));
		 
		 columns.save(toDo);
		 
		 assertTrue(tasks.findByColumn(toDo).size()==2);
		 
		 projects.deleteAll();
		 
	}

	@Test
	public void littleScenario() {

		// on cree un projet et on garde son id ds un coin
		Long idKata = projects.save(new Project("Kata")).getId();

		// On le retrieve par l'id puis on y ajoute des columns
		Project pKata = projects.findById(idKata).get();
		pKata.addColumn(new Column("Backlog"));
		pKata.addColumn(new Column("ToDo"));
		projects.save(pKata);

		// On retrieve encore pour etre sur des modifs le project et on check
		pKata = projects.findById(idKata).get();
		assertTrue(pKata.getColumns().size() == 2);

		// On retrive une colum pour y ajouter des tasks
		Column backLog = pKata.getColumns().get(0);
		Task arch = new Task("Archi");
		backLog.addTask(arch);

		// ON save le project et tous ses sous relations / enfants le sont aussi :)
		// projects.save(pKata);

		// On peut aussi faire persister la task en sauvant la column, normal
		// columns.save(backLog);

		// OU bien tout simplement depuis le repo de tasks mais la alors il faudra
		// preciser
		// la column avec un set ou depuis un constructeur
		
		arch.setColumn(backLog);
		tasks.save(arch);

		// On retrieve encore pour etre sur de l 'ajout de la task et on check
		pKata = projects.findById(idKata).get();
		assertTrue(pKata.getColumns().get(0).getTasks().get(0).getName().equals("Archi"));

		// Tests de crud de tasks et de columns
		// .......

		// PUis on le supprime
		projects.deleteById(idKata);
	}
}
