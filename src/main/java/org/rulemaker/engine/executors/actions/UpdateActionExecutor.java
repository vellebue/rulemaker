package org.rulemaker.engine.executors.actions;

import java.util.List;

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
	
	public void onExecute(List<Object> conditionMatchingObjects) throws ExecutionException {
		Integer targetIndex = (Integer) this.getSharpArgumentsMap().get(SHARP_ARGUMENT_TARGET);
		if ((conditionMatchingObjects == null) || (targetIndex > conditionMatchingObjects.size())) {
			throw new ExecutionException("There is no condition object with index: " + targetIndex + " for this rule");
		} else {
			Object targetObject = conditionMatchingObjects.get(targetIndex - 1);
			putArgumentsIntoTargetObject(targetObject, getRegularArgumentsMap());
		}
	}

}
