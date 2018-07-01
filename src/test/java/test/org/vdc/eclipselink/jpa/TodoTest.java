package test.org.vdc.eclipselink.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.vdc.eclipselink.jpa.entities.Todo;

@RunWith(JUnit4.class)
public class TodoTest {

	private static final String PERSISTENCE_UNIT_NAME = "todos";
	private static EntityManagerFactory factory;

	@Test
	public void testTodo() {

		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		EntityManager em = factory.createEntityManager();
		// read the existing entries and write to console
		Query q = em.createQuery("select t from Todo t");

		List<Todo> todoList = q.getResultList();

		for (Todo todo : todoList) {
			System.out.println(todo);
		}
		System.out.println("Size: " + todoList.size());

		// create new todo
		em.getTransaction().begin();
		Todo todo = new Todo();
		todo.setSummary("This is a test");
		todo.setDescription("This is a test");
		em.persist(todo);
		em.getTransaction().commit();

		em.close();

	}

}
