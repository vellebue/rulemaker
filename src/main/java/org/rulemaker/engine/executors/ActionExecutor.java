package org.rulemaker.engine.executors;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Common interface for actions to be performed
 * in rules.
 * 
 * @author &Aacute;ngel Garc&iacute;a Bastanchuri
 *
 */
public interface ActionExecutor {
	
	/**
	 * Validates action arguments, action arguments are
	 * given into two maps: one containing arguments preceded
	 * by a '#' and other one containing arguments that correspond
	 * to setter methods in target objects for the action. Keys in maps
	 * represent parameter names and their matching values correspond to
	 * current values for those arguments.
	 * 
	 * @param sharpArgumentsMap A map containing arguments preceded by
	 * '#' in action definition with their corresponding values. 
	 * Notice that these arguments (their keys) are stored into map
	 * without preceding '#' character.
	 * 
	 * @param regularArgumentsMap A map containing arguments corresponding
	 * to setters in target objects.
	 * 
	 * @return A list containing validation errors for the arguments or null
	 *         or an empty list if no validation errors are detected.
	 *          
	 */
	public List<ActionError> validate(Map<String, Object> sharpArgumentsMap,
									  Map<String, Object> regularArgumentsMap);
	
	/**
	 * Executes the action for this action in rule.
	 * 
	 * @param conditionMatchingObjects A list containing the objects matched
	 *                                 into the condition list in this rule.
	 *                                 Objects are given in the same order they
	 *                                 have been matched by the conditions list
	 *                                 clause.
	 *                                 
	 * @throws ExecutionException If there are any problems while executing this action.
	 */
	public void execute(List<Object> conditionMatchingObjects) throws ExecutionException;

}
