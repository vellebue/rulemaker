package org.rulemaker.engine.executors.actions;

import java.util.Map;

import org.rulemaker.engine.executors.exception.ExecutionException;

/**
 * Executor used to update facts given the fact to be updated.
 * <br>
 * Arguments: 
 * <ul>
 *   <li>
 *   #target: Number of condition in condition list 
 *                     (from 1 to N) that matches the selected fact 
 *                     to be updated.
 *   </li>
 * </ul>
 * 
 * @author &Aacute;ngel Garc&iacute;a Bastanchuri
 *
 */
public class UpdateActionExecutor extends AbstractTargetActionExecutor {
	
	public void onExecute(Map<String, Object> conditionMatchingMap) throws ExecutionException {
		Integer targetIndex = (Integer) this.getSharpArgumentsMap().get(SHARP_ARGUMENT_TARGET);
		String targetFactName = "_" + targetIndex;
		if ((conditionMatchingMap == null) || (conditionMatchingMap.get(targetFactName) == null)) {
			throw new ExecutionException("There is no condition object with name: " + targetFactName + " for this rule");
		} else {
			Object targetObject = conditionMatchingMap.get(targetFactName);
			putArgumentsIntoTargetObject(targetObject, getRegularArgumentsMap());
		}
	}

}
