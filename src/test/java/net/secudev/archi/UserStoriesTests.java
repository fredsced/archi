package net.secudev.archi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserStoriesTests {

	//TDD style avec des users stories vides qui echouent tant que la feature n est pas presente du sprint
	//C'est ici que l'on va tester avec spring security pour verifier si en tant que untel
	//Car désomrais, depuis que l'on a une couche service, ce sera ici que l'on placera la securité sur les méthodes critiques
	//La couche service est une couche métier ou on devra placer les annotations @preauthorize au niveau méthodes

	@Test
	public void test()
	{}
}
