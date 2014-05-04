package org.rulemaker.engine;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.*;

import org.rulemaker.engine.matcher.Person;
import org.rulemaker.model.Rule;

public class BaseEngineTest {
	
	private class TestEngine extends BaseEngine {

		public TestEngine(List<Rule> rules) {
			super(rules);
		}

		public TestEngine(String rulesText) throws EngineException {
			super(rulesText);
		}		
	}
	
	public static class NullablePerson extends Person {

		public NullablePerson(String name) {
			super(name);
		}
		
		public NullablePerson() {
			super(null);
		}
		
	}
	
	@Test
	public void shouldPerformAnEngineStepCorrectly() throws Exception {
		TestEngine engine = new 
				TestEngine("(age = X) -> update(#target = 1, age = ${2 * X})");
		Person john = new Person("John");
		john.setAge(15);
		engine.addFact(john);
		boolean executed = engine.runStep();
		assertTrue(executed);
		assertEquals(1, engine.getEngineContext().getFactList().size());
		Person johnUpdated = (Person) engine.getEngineContext().getFactList().get(0);
		assertEquals(new Integer(30), johnUpdated.getAge());
	}
	
	@Test
	public void shouldPerformAnEngineStepWithTwoActionsCorrectly() throws Exception {
		TestEngine engine = new 
				TestEngine("(age = X) -> update(#target = 1, age = ${2 * X}) update(#target = 1, name = 'Jaimie')");
		Person john = new Person("John");
		john.setAge(15);
		engine.addFact(john);
		boolean executed = engine.runStep();
		assertTrue(executed);
		assertEquals(1, engine.getEngineContext().getFactList().size());
		Person johnUpdated = (Person) engine.getEngineContext().getFactList().get(0);
		assertEquals(new Integer(30), johnUpdated.getAge());
		assertEquals("Jaimie", johnUpdated.getName());
	}
	
	@Test
	public void shouldPerformAnEngineStepWithTwoConditionsCorrectly() throws Exception {
		TestEngine engine = new 
				TestEngine("(name = 'John', #constraint = ${age >= 18}) "
						+  "(name = 'Johanna', #constraint = ${age >= 18}) -> "
						+  " create(#className = 'Person', name = 'Johnny', age = 0) ");
		Person john = new Person("John");
		john.setAge(24);
		Person johanna = new Person("Johanna");
		johanna.setAge(22);
		engine.registerClassSinonym("Person", NullablePerson.class);
		engine.addFact(john);
		engine.addFact(johanna);
		boolean executed = engine.runStep();
		assertTrue(executed);
		assertEquals(3, engine.getEngineContext().getFactList().size());
		Person jonny = findPerson("Johnny", engine.getEngineContext().getFactList());
		assertNotNull(jonny);
		assertEquals(new Integer(0), jonny.getAge());
	}
	
	private Person findPerson(String name, List<Object> personFactsList) {
		Person personFound = null;
		Iterator<Object> iterator = personFactsList.iterator();
		while ((personFound == null) && iterator.hasNext()) {
			Object object = iterator.next();
			if (object instanceof Person) {
				Person aPerson = (Person) object;
				if (aPerson.getName().equals(name)) {
					personFound = aPerson;
				}
			}
		}
		return personFound;
	}

}
