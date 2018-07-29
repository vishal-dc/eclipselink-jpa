package test.org.vdc.eclipselink.jpa;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.vdc.eclipselink.jpa.entities.Employee;
import org.vdc.eclipselink.jpa.entities.Project;

@RunWith(JUnit4.class)
public class EmployeeProjectTests {

	private static final String PERSISTENCE_UNIT_NAME = "todos";
	private static EntityManagerFactory factory;

	@BeforeClass
	public static void setup() {

		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		EntityManager em = factory.createEntityManager();
		
		em.getTransaction().begin();
		
		Project p1 = new Project();
		p1.setName("Hello World Project");
		em.persist(p1);
		
		Employee e1 =  new Employee();
		e1.setName("Hello");
		
		Employee e2 = new Employee();
		e2.setName("World");
		
		Set<Employee> emps = new HashSet<>();
		emps.add(e1);
		emps.add(e2);
		
		e1.setProject(p1);
		
		e2.setProject(p1);
		
		p1.setEmployees(emps);
		
		
		em.getTransaction().commit();
		
		
		System.out.println("Project 1 " + p1.getId());
		em.close();

	}

	@Test
	public void testSearch() {
		EntityManager em = factory.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Project> q = cb.createQuery(Project.class);
		Root<Project> root = q.from(Project.class);
		
		ParameterExpression<String> nameParam = cb.parameter(String.class);
		ParameterExpression<Boolean> statusParam = cb.parameter(Boolean.class);
		Predicate namePredicate = cb.like(root.get("name"), "%ello%");
		Predicate statusPredicate = cb.equal(root.get("isActive"), true);
		
		Predicate employeeStatusPredicate = 
				root.g
				cb.equal(root.get("employees.isActive"), true);
		
		
		
		q.select(root).where(cb.and(namePredicate, statusPredicate));
		TypedQuery<Project> tq =  em.createQuery(q);
		
		
		
		tq.getResultList().forEach(p -> System.out.println(p));
		
		
		
//		q.from(arg0)
		
	}

}
