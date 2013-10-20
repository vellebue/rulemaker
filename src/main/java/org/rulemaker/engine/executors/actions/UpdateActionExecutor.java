package org.rulemaker.engine.executors.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.rulemaker.engine.executors.ActionError;
import org.rulemaker.engine.executors.BaseActionExecutor;
import org.rulemaker.engine.executors.exception.ExecutionException;

public class UpdateActionExecutor extends BaseActionExecutor {
	
	private static final String SHARP_ARGUMENT_TARGET = "target";

	public List<ActionError> validate(Map<String, Object> sharpArgumentsMap,
			Map<String, Object> regularArgumentsMap) {
		List<ActionError> errors = new ArrayList<ActionError>();
		// Look for #target argument.
		Object value = sharpArgumentsMap.get("target");
		if ((value == null) || 
		    !(value instanceof Integer) ||
		    (Integer.parseInt(value.toString()) <= 0)) {
			ActionError error = new ActionError();
			error.setDescription("There must be a #" + SHARP_ARGUMENT_TARGET + " argument " + 
		             "and its value should be an integer " +
		             "greather or equal to 1");
			errors.add(error);
		}
		return errors;
	}

	public void execute(List<Object> conditionMatchingObjects) throws ExecutionException {
		Integer targetIndex = (Integer) this.getSharpArgumentsMap().get(SHARP_ARGUMENT_TARGET);
		if ((conditionMatchingObjects == null) || (targetIndex > conditionMatchingObjects.size())) {
			throw new ExecutionException("There is no condition object with index: " + targetIndex + " for this rule");
		} else {
			Object targetObject = conditionMatchingObjects.get(targetIndex - 1);
			putArgumentsIntoTargetObject(targetObject, getRegularArgumentsMap());
		}
	}

}
