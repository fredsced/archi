package net.secudev.archi;


import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import net.secudev.archi.model.Project;
import net.secudev.archi.service.impl.ProjectService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ControllersTests {

	@Autowired
	private MockMvc mvc;

//	@Autowired
//	private ProjectService serviceLeVrai;
	
	@MockBean
	private ProjectService serviceLeFaux;
	
	@Before
	public void beforeTest()
	{
		//serviceLeVrai.removeAllProjects();
	}

	@Test
	public void createAPojectThenCheckTheCreatedEntityIsSent() throws Exception {
		
		//serviceLeVrai.createOrUpdateProject(new Project("kanban"));
		List<Project> fauxProjets = new ArrayList<>();
		fauxProjets.add(new Project("kanban"));
		
		when(serviceLeFaux.allProject()).thenReturn(fauxProjets);
		mvc.perform(get("/api/v1/projects")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk())			      
			      .andExpect(jsonPath("$[0].name", is("kanban")));			
	}
}
