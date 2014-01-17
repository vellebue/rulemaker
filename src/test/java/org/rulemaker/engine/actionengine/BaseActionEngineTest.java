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
import org.rulemaker.engine.executors.exception.ExecutionException;
import org.rulemaker.engine.matcher.Person;
import org.rulemaker.model.Rule;
import org.rulemaker.parser.RulesParser;

public class BaseActionEngineTest {
	
	/**
	 * A test action executor that duplicates a field (integer) value
	 * given the number of condition fact object and its name.
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

		public void onExecute(List<Object> conditionMatchingObjects)
				throws ExecutionException {
			Integer index = (Integer) getSharpArgumentsMap().get("target");
			Person person = (Person) conditionMatchingObjects.get(index - 1);
			person.setAge(2 * person.getAge());
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

}
