package org.rulemaker.engine.executors.actions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.rulemaker.engine.EngineContext;
import org.rulemaker.engine.executors.ActionError;
import org.rulemaker.engine.matcher.Person;

import static org.junit.Assert.*;

public class DeleteActionExecutorTest {

	@Test
	public void shouldRemoveAFactFromSingleListFactBase() throws Exception {
		EngineContext context = new EngineContext(null);
		Person person = new Person("John");
		context.addFact(person);
		Map<String, Object> sharpArgumentsMap = new HashMap<String, Object>();
		sharpArgumentsMap.put("target", 1);
		DeleteActionExecutor executor = new DeleteActionExecutor();
		List<ActionError> errors = executor.validate(sharpArgumentsMap, new HashMap<String, Object>());
		// There should be no errors
		assertTrue((errors == null) || (errors.size() == 0));
		executor.setEngineContext(context);
		executor.execute(Arrays.asList(new Object[]{person}));
		// Person object must be deleted from facts list
		assertEquals(0, context.getFactList().size());
	}
	
	@Test
	public void shouldRemoveAFactFromADomain() throws Exception {
		EngineContext context = new EngineContext(null);
		Person person = new Person("John");
		context.addFact("people", person);
		Map<String, Object> sharpArgumentsMap = new HashMap<String, Object>();
		sharpArgumentsMap.put("target", 1);
		DeleteActionExecutor executor = new DeleteActionExecutor();
		List<ActionError> errors = executor.validate(sharpArgumentsMap, new HashMap<String, Object>());
		// There should be no errors
		assertTrue((errors == null) || (errors.size() == 0));
		executor.setEngineContext(context);
		executor.execute(Arrays.asList(new Object[]{person}));
		// Person object must be deleted from facts list
		assertEquals(0, context.getFactBase().get("people").size());
	}
}
