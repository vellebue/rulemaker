package org.rulemaker.engine.executors;

import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.rulemaker.engine.EngineContext;
import org.rulemaker.engine.executors.exception.ExecutionException;

/**
 * Base class for all action executors. It contains
 * base methods to perform custom actions.
 * 
 * @author &Aacute;ngel Garc&iacute;a Bastanchuri
 *
 */
public abstract class BaseActionExecutor implements ActionExecutor {
	
	private Map<String, Object> sharpArgumentsMap;
	private Map<String, Object> regularArgumentsMap;
	private EngineContext engineContext;
	private boolean validated = false;

	/**
	 * Gets the current map of arguments preceded with '#'.
	 * 
	 * @return The current map of sharp arguments.
	 * 
	 */
	protected Map<String, Object> getSharpArgumentsMap() {
		return sharpArgumentsMap;
	}
	
	/**
	 * Sets the current map of arguments preceded with '#'.
	 * 
	 * @param sharpArgumentsMap The map of sharp arguments to be assigned.
	 * 
	 */
	private void setSharpArgumentsMap(Map<String, Object> sharpArgumentsMap) {
		this.sharpArgumentsMap = sharpArgumentsMap;
	}
	
	/**
	 * Gets the current map of regular arguments.
	 * 
	 * @return The current map of regular arguments.
	 * 
	 */
	protected Map<String, Object> getRegularArgumentsMap() {
		return regularArgumentsMap;
	}
	
	/**
	 * Sets the current map of regular arguments.
	 * 
	 * @param regularArgumentsMap the current map of regular arguments.
	 * 
	 */
	private void setRegularArgumentsMap(Map<String, Object> regularArgumentsMap) {
		this.regularArgumentsMap = regularArgumentsMap;
	}
	
	/**
	 * Gets the engine context for this action executor.
	 * 
	 * @return Current engine context.
	 * 
	 */
	protected EngineContext getEngineContext() {
		return engineContext;
	}

	/**
	 * Sets the current engine context for this executor.
	 * 
	 * @param engineContext the current engine context to be assigned.
	 * 
	 */
	public void setEngineContext(EngineContext engineContext) {
		this.engineContext = engineContext;
	}
	
	/**
	 * Performs validation process for this action arguments.
	 * It uses {@link org.rulemaker.engine.executors.ActionExecutor onValidate}
	 * to perform validation process.
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
	 * @see org.rulemaker.engine.executors.ActionExecutor
	 *          
	 */
	public final List<ActionError> validate(Map<String, Object> sharpArgumentsMap,
			  Map<String, Object> regularArgumentsMap) {
		List<ActionError> errors = onValidate(sharpArgumentsMap, regularArgumentsMap);
		setSharpArgumentsMap(sharpArgumentsMap);
		setRegularArgumentsMap(regularArgumentsMap);
		if ((errors == null) || (errors.size() == 0)) {
			validated = true;
		}
		return errors;
	}
	
	/**
	 * Executes the action for this action in rule conforming to the implementation provided
	 * by {@link org.rulemaker.engine.executors.ActionExecutor onExecute}.
	 * 
	 * @param conditionMatchingObjects A list containing the objects matched
	 *                                 into the condition list in this rule.
	 *                                 Objects are given in the same order they
	 *                                 have been matched by the conditions list
	 *                                 clause.
	 *                                 
	 * @throws ExecutionException If no previous validation has been performed or previous
	 *                            validation has error, or if reported an exception during execution.
	 * 
	 * @see org.rulemaker.engine.executors.ActionExecutor
	 */
	public final void execute(List<Object> conditionMatchingObjects) throws ExecutionException {
		if (validated) {
			onExecute(conditionMatchingObjects);
		} else {
			throw new ExecutionException("Execution not performed with previous validation or there were validation errors");
		}
	}
	
	/**
	 * Convenience method to transfer argument values in a map into
	 * a target object that needs to be updated conforming those values.
	 * 
	 * @param targetObject The object to be updated, for each key in map
	 *                     there should be a setter method in the target
	 *                     object conforming to the key name to update the value, 
	 *                     otherwise the value should be ignored.
	 *                     
	 * @param argumentsMap The map containing the name of the fields to be updated into
	 *                     target object and their new values.
	 *                     
	 * @throws ExecutionException If there is no target attribute in targetObject matching
	 * 							  some of the keys given in arguments map or if there is
	 * 							  a mismatch in argument type.
	 * 
	 * @throws NullPointerException If targetObject is null or argumentsMap is null.
	 *                            
	 */
	protected void putArgumentsIntoTargetObject(Object targetObject, 
			                                    Map<String, Object> argumentsMap) throws ExecutionException {
		for (String aKey : argumentsMap.keySet()) {
			if (PropertyUtils.isWriteable(targetObject, aKey)) {
				try {
					Class<?> targetArgumentType = PropertyUtils.getPropertyType(targetObject, aKey);
					Object value = argumentsMap.get(aKey);
					Class<?> valueType = (value != null) ? value.getClass() : Object.class;
					if (targetArgumentType.isAssignableFrom(valueType) || (value == null)) {
						PropertyUtils.setProperty(targetObject, aKey, value);
					} else {
						throw new ExecutionException("Incompatible types in target object: "
								+ "expected " + targetArgumentType.getName() + " but was " 
								+ valueType.getName());
					}
				} catch (Exception e) {
					// This exception might not be thrown.
					throw new ExecutionException(e);
				}
			} else {
				throw new ExecutionException("There is no property named " + aKey + 
						" in object with type: " + targetObject.getClass().getName());
			}
		}
	}
}
