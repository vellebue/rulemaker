package org.rulemaker.engine.executors;

import java.util.List;
import java.util.Map;

import org.rulemaker.engine.executors.exception.ExecutionException;

/**
 * Common interface for actions to be performed
 * in rules.
 * 
 * @author &Aacute;ngel Garc&iacute;a Bastanchuri
 *
 */
public interface ActionExecutor {
	
	/**
	 * Performs validation action arguments, action arguments are
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
	public List<ActionError> onValidate(Map<String, Object> sharpArgumentsMap,
									  Map<String, Object> regularArgumentsMap);
	
	/**
	 * Performs action execution for this action in rule.
	 * 
	 * @param conditionMatchingMap A map containing the objects matched
	 *                             by a matching process. The matching objects
	 *                             should be named as "_1", "_2" ... "_n" respectively
	 *                             for the first, second... and nth matching fact in the
	 *                             conditions rule list. The map must also include the variables
	 *                             referred by the conditions and its values.  
	 *                                 
	 * @throws ExecutionException If there are any problems while executing this action.
	 */
	public void onExecute(Map<String, Object> conditionMatchingMap) throws ExecutionException;

}
