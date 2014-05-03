package org.rulemaker.engine.executors;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.rulemaker.engine.executors.exception.ExecutionException;
import org.rulemaker.engine.matcher.Person;

public class BaseActionExecutorTest {
	
	private class DummyPersonExecutor extends BaseActionExecutor {
		
		private Person targetPerson; 

		public List<ActionError> onValidate(
				Map<String, Object> sharpArgumentsMap,
				Map<String, Object> regularArgumentsMap) {
			return null;
		}

		public void onExecute(Map<String, Object> conditionMatchingMap)
				throws ExecutionException {
			targetPerson = new Person("");
			putArgumentsIntoTargetObject(targetPerson, getRegularArgumentsMap());
		}

		public Person getTargetPerson() {
			return targetPerson;
		}
	}
	
	@Test(expected = ExecutionException.class)
	public void shouldPerformExecutionExceptionWhenExecutingwithoutValidating() {
		DummyPersonExecutor executor = new DummyPersonExecutor();
		executor.execute(null);
	}
	
	@Test
	public void shouldPutArgumentsIntoTargetObjectWhenMapIsOk() throws Exception {
		Map<String, Object> originMap = new HashMap<String, Object>();
		originMap.put("name", "John");
		originMap.put("age", 16);
		originMap.put("height", 1.72F);
		originMap.put("salary", 1345.23);
		DummyPersonExecutor executor = new DummyPersonExecutor();
		executor.validate(new HashMap<String, Object>(), originMap);
		executor.execute(null);
		Person targetPerson = executor.getTargetPerson();
		assertEquals("John", targetPerson.getName());
		assertEquals(new Integer(16), targetPerson.getAge());
		assertEquals(new Float(1.72F), targetPerson.getHeight(), 0.0);
		assertEquals(new Double(1345.23), targetPerson.getSalary());
	}
	
	@Test
	public void shouldPutArgumentsIntoTargetObjectWhenMapIsOkWithANullValue() throws Exception {
		Map<String, Object> originMap = new HashMap<String, Object>();
		originMap.put("name", null);
		originMap.put("age", 16);
		originMap.put("height", 1.72F);
		originMap.put("salary", 1345.23);
		DummyPersonExecutor executor = new DummyPersonExecutor();
		executor.validate(new HashMap<String, Object>(), originMap);
		executor.execute(null);
		Person targetPerson = executor.getTargetPerson();
		assertNull(targetPerson.getName());
		assertEquals(new Integer(16), targetPerson.getAge());
		assertEquals(new Float(1.72F), targetPerson.getHeight(), 0.0);
		assertEquals(new Double(1345.23), targetPerson.getSalary());
	}
	
	@Test(expected = org.rulemaker.engine.executors.exception.ExecutionException.class)
	public void shouldThrowAnExceptionWhenThereIsAnUnknownFieldNameInMap() throws Exception {
		Map<String, Object> originMap = new HashMap<String, Object>();
		originMap.put("unknownField", "John");
		DummyPersonExecutor executor = new DummyPersonExecutor();
		executor.validate(new HashMap<String, Object>(), originMap);
		executor.execute(null);
	}
	
	@Test(expected = org.rulemaker.engine.executors.exception.ExecutionException.class)
	public void shouldThrowAnExceptionWhenThereIsAFieldInMapWhoseValueIsFromAnIncompatibleType() throws Exception {
		Map<String, Object> originMap = new HashMap<String, Object>();
		originMap.put("name", 16.9);
		DummyPersonExecutor executor = new DummyPersonExecutor();
		executor.validate(new HashMap<String, Object>(), originMap);
		executor.execute(null);
	}

}
