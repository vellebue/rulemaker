package org.rulemaker.engine.executors.actions;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.rulemaker.engine.EngineContext;
import org.rulemaker.engine.executors.ActionError;
import org.rulemaker.engine.executors.exception.ExecutionException;
import org.rulemaker.engine.matcher.Person;

public class UpdateActionExecutorTest {
	
	@Test
	public void shouldReportAValidationErrorWhenNoTargetArgumentIsGiven() throws Exception {
		Map<String, Object> sharpArgumentsMap = new HashMap<String, Object>();
		Map<String, Object> regularArgumentsMap = new HashMap<String, Object>();
		sharpArgumentsMap.put("targetObject", 1);
		UpdateActionExecutor executor = new UpdateActionExecutor();
		List<ActionError> errors = executor.validate(sharpArgumentsMap, regularArgumentsMap);
		assertNotNull(errors);
		assertTrue(errors.size() > 0);
		assertEquals("There must be a #target argument " + 
		             "and its value should be an integer " +
		             "greather or equal to 1", errors.get(0).getDescription());
		
	}
	
	@Test
	public void shouldReportAValidationErrorWhenANoNumericTargetArgumentIsGiven() throws Exception {
		Map<String, Object> sharpArgumentsMap = new HashMap<String, Object>();
		Map<String, Object> regularArgumentsMap = new HashMap<String, Object>();
		sharpArgumentsMap.put("target", "One");
		UpdateActionExecutor executor = new UpdateActionExecutor();
		List<ActionError> errors = executor.validate(sharpArgumentsMap, regularArgumentsMap);
		assertNotNull(errors);
		assertTrue(errors.size() > 0);
		assertEquals("There must be a #target argument " + 
		             "and its value should be an integer " +
		             "greather or equal to 1", errors.get(0).getDescription());
	}
	
	@Test
	public void shouldUpdateGivenAttributeOnGivenObjectOnAValidUpdateActionExecutor() throws Exception {
		Person personMatched1 = new Person("John");
		Person personMatched2 = new Person("Jaimie");
		Map<String, Object> sharpArgumentsMap = new HashMap<String, Object>();
		Map<String, Object> regularArgumentsMap = new HashMap<String, Object>();
		EngineContext engineContext = new EngineContext(null);
		sharpArgumentsMap.put("target", 2);
		regularArgumentsMap.put("name", "Jeremy");
		engineContext.addFact(personMatched1);
		engineContext.addFact(personMatched2);
		UpdateActionExecutor executor = new UpdateActionExecutor();
		executor.setEngineContext(engineContext);
		List<ActionError> errors = executor.validate(sharpArgumentsMap, regularArgumentsMap);
		// There should be no errors
		assertTrue((errors == null) || (errors.size() == 0));
		// Execution
		Map<String, Object> executionMap = new HashMap<String, Object>();
		executionMap.put("_1", personMatched1);
		executionMap.put("_2", personMatched2);
		executor.execute(executionMap);
		assertEquals("Jeremy", personMatched2.getName());
	}
	
	@Test(expected = ExecutionException.class)
	public void shouldFailWhenTryingToUpdateANonExistingReferredFact() throws Exception {
		Person personMatched1 = new Person("John");
		Map<String, Object> sharpArgumentsMap = new HashMap<String, Object>();
		Map<String, Object> regularArgumentsMap = new HashMap<String, Object>();
		EngineContext engineContext = new EngineContext(null);
		engineContext.addFact(personMatched1);
		sharpArgumentsMap.put("target", 2);
		regularArgumentsMap.put("name", "Jeremy");
		UpdateActionExecutor executor = new UpdateActionExecutor();
		executor.setEngineContext(engineContext);
		List<ActionError> errors = executor.validate(sharpArgumentsMap, regularArgumentsMap);
		// There should be no errors
		assertTrue((errors == null) || (errors.size() == 0));
		// Execution
		Map<String, Object> executionMap = new HashMap<String, Object>();
		executionMap.put("_1", personMatched1);
		executor.execute(executionMap);
	}

}
