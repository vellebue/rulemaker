package org.rulemaker.engine.actionengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import static org.junit.Assert.*;

import org.rulemaker.engine.EngineContext;
import org.rulemaker.engine.EngineException;
import org.rulemaker.engine.executors.ActionError;
import org.rulemaker.engine.executors.BaseActionExecutor;
import org.rulemaker.engine.executors.actions.UpdateActionExecutor;
import org.rulemaker.engine.executors.exception.ExecutionException;
import org.rulemaker.engine.matcher.Person;
import org.rulemaker.model.Rule;
import org.rulemaker.parser.RulesParser;

public class BaseActionEngineTest {
	
	/**
	 * A test action executor that duplicates a field (integer) value
	 * named "age" given the number of condition fact object containing
	 * this field.
	 * 
	 * @author &Aacute;ngel Garc&iacute;a Bastanchuri
	 *
	 */
	public static class DuplicateAgeActionExecutor extends BaseActionExecutor {

		public DuplicateAgeActionExecutor() {
			super();
		}

		public List<ActionError> onValidate(
				Map<String, Object> sharpArgumentsMap,
				Map<String, Object> regularArgumentsMap) {
			List<ActionError> errors = new ArrayList<ActionError>();
			if (sharpArgumentsMap.get("target") == null) {
				ActionError error = new ActionError();
				error.setDescription("target argument required");
				errors.add(error);
			}
			return errors;
		}

		public void onExecute(Map<String, Object> conditionMatchingMap)
				throws ExecutionException {
			Integer index = (Integer) getSharpArgumentsMap().get("target");
			String factName = "_" + index;
			Person person = (Person) conditionMatchingMap.get(factName);
			person.setAge(2 * person.getAge());
		}
		
	}
	
	/**
	 * A test action executor that increments a field (Integer)
	 * named age given the number of condition matching object
	 * (into the field #target) and the value to increment the age value
	 * (into a field named #value)
	 * 
	 * @author &Aacute;ngel Garc&iacute;a Bastanchuri
	 *
	 */
	public static class AdderAgeActionExecutor extends BaseActionExecutor {

		public List<ActionError> onValidate(
				Map<String, Object> sharpArgumentsMap,
				Map<String, Object> regularArgumentsMap) {
			List<ActionError> errors = new ArrayList<ActionError>();
			if (sharpArgumentsMap.get("target") == null) {
				ActionError error = new ActionError();
				error.setDescription("target argument required");
				errors.add(error);
			}
			if (sharpArgumentsMap.get("value") == null) {
				ActionError error = new ActionError();
				error.setDescription("value argument required");
				errors.add(error);
			}
			return errors;
		}

		public void onExecute(Map<String, Object> conditionMatchingMap)
				throws ExecutionException {
			Integer index = (Integer) getSharpArgumentsMap().get("target");
			String factName = "_" + index;
			Integer value = (Integer) getSharpArgumentsMap().get("value");
			Person person = (Person) conditionMatchingMap.get(factName);
			person.setAge(person.getAge() + value);
		}
		
	}
	
	@Test
	public void shouldExecuteAnActionExecutorCorrectly() throws Exception {
		BaseActionEngine actionEngine = new BaseActionEngine();
		List<Rule> rulesList =  RulesParser.getInstance().parseRules("-> duplicate(#target = 1)");
		Person person = new Person("John Doe");
		person.setAge(13);
		EngineContext context = new EngineContext(rulesList);
		context.addFact(person);
		actionEngine.setEngineContext(context);
		actionEngine.addExecutorClass("duplicate", DuplicateAgeActionExecutor.class);
		Map<String, Object> matchingConditionVariables = new HashMap<String, Object>();
		matchingConditionVariables.put("_1", person);
		actionEngine.executeAction(matchingConditionVariables, rulesList.get(0).getActionList().get(0));
		Person personUpdated = (Person) context.getFactList().get(0);
		assertEquals(new Integer(26), personUpdated.getAge());
	}
	
	@Test
	public void shouldExecuteAnActionExecutorCorrectlyWithFactVariables() throws Exception {
		BaseActionEngine actionEngine = new BaseActionEngine();
		List<Rule> rulesList =  RulesParser.getInstance().parseRules("-> add(#target = 1, #value=${X})");
		Person person = new Person("John Doe");
		person.setAge(13);
		EngineContext context = new EngineContext(rulesList);
		context.addFact(person);
		actionEngine.setEngineContext(context);
		actionEngine.addExecutorClass("add", AdderAgeActionExecutor.class);
		
		Map<String, Object> matchingConditionVariables = new HashMap<String, Object>();
		matchingConditionVariables.put("_1", person);
		matchingConditionVariables.put("X", 2);
		actionEngine.executeAction(matchingConditionVariables, rulesList.get(0).getActionList().get(0));
		Person personUpdated = (Person) context.getFactList().get(0);
		assertEquals(new Integer(15), personUpdated.getAge());
	}
	
	@Test(expected = EngineException.class)
	public void shouldExecuteAnActionExecutorWithErrorsWhenAValidatingErrorIsProduced() throws Exception {
		BaseActionEngine actionEngine = new BaseActionEngine();
		// "targets" is not right, it should be target
		List<Rule> rulesList =  RulesParser.getInstance().parseRules("-> duplicate(#targets = 1)");
		Person person = new Person("John Doe");
		person.setAge(13);
		EngineContext context = new EngineContext(rulesList);
		context.addFact(person);
		actionEngine.setEngineContext(context);
		actionEngine.addExecutorClass("duplicate", DuplicateAgeActionExecutor.class);
		Map<String, Object> matchingConditionVariables = new HashMap<String, Object>();
		matchingConditionVariables.put("_1", person);
		actionEngine.executeAction(matchingConditionVariables, rulesList.get(0).getActionList().get(0));
	}
	
	@Test(expected = EngineException.class)
	public void shouldExecuteAnActionExecutorWithErrorsWhenInvokingUnregisteredAction() throws Exception {
		BaseActionEngine actionEngine = new BaseActionEngine();
		List<Rule> rulesList =  RulesParser.getInstance().parseRules("-> duplicate(#targets = 1)");
		Person person = new Person("John Doe");
		person.setAge(13);
		EngineContext context = new EngineContext(rulesList);
		context.addFact(person);
		actionEngine.setEngineContext(context);
		// duplicate action is NOT registered now
		//actionEngine.addExecutorClass("duplicate", DuplicateAgeActionExecutor.class);
		Map<String, Object> matchingConditionVariables = new HashMap<String, Object>();
		matchingConditionVariables.put("_1", person);
		actionEngine.executeAction(matchingConditionVariables, rulesList.get(0).getActionList().get(0));
	}
	
	@Test
	public void shouldExecuteAnUpdateActionExecutorWithACompleteUpdateSetProperly() throws Exception {
		BaseActionEngine actionEngine = new BaseActionEngine();
		List<Rule> rulesList =  RulesParser.getInstance().parseRules(
				"-> update(#target=1, name='Jeremy Irons', age=${_1.getAge() + 3}, salary=1280.33, height=1.83)");
		Person person = new Person("John Doe");
		person.setAge(28);
		EngineContext context = new EngineContext(rulesList);
		context.addFact(person);
		actionEngine.setEngineContext(context);
		actionEngine.addExecutorClass("update", UpdateActionExecutor.class);
		Map<String, Object> matchingConditionVariables = new HashMap<String, Object>();
		matchingConditionVariables.put("_1", person);
		actionEngine.executeAction(matchingConditionVariables, rulesList.get(0).getActionList().get(0));
		assertEquals("Jeremy Irons", person.getName());
		assertEquals(new Integer(31), person.getAge());
		assertEquals(new Double(1280.33), person.getSalary());
		assertEquals(new Float(1.83), person.getHeight());
	}
}
