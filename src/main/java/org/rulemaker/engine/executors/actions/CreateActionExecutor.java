package org.rulemaker.engine.executors.actions;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.rulemaker.engine.EngineContext;
import org.rulemaker.engine.executors.ActionError;
import org.rulemaker.engine.executors.BaseActionExecutor;
import org.rulemaker.engine.executors.exception.ExecutionException;

/**
 * Executor used to create facts instances and to initialize fields.
 * <br>
 * Arguments: 
 *  <ul>
 *   <li>
 *   #className: The name of the class used as a reference to create 
 *               the instance, it must have a zero argument constructor.
 *               The class name can be a synonym class name (
 *               registered in {#link org.rulemaker.engine.EngineContext EngineContext}) 
 *               or a full qualified class name as usual in Java.
 *   </li>
 *   <li>
 *   #domain: (Optional) The name of the domain where the new fact should be 
 *                        registered. It must be a String. 
 *   </li>
 *  </ul> 
 * @author &Aacute;ngel Garc&iacute;a Bastanchuri
 * 
 * @see org.rulemaker.engine.EngineContext
 */
public class CreateActionExecutor extends BaseActionExecutor {
	
	private static final String SHARP_ARGUMENT_CLASS_NAME = "className";
	private static final String SHARP_ARGUMENT_DOMAIN = "domain";
	
	private Class<?> targetType;
	private String targetDomainName;

	public List<ActionError> onValidate(Map<String, Object> sharpArgumentsMap,
			Map<String, Object> regularArgumentsMap) {
		List<ActionError> errors = new ArrayList<ActionError>();
		if ((sharpArgumentsMap.get(SHARP_ARGUMENT_CLASS_NAME) != null) &&
		    sharpArgumentsMap.get(SHARP_ARGUMENT_CLASS_NAME) instanceof String) {
			String className = (String) sharpArgumentsMap.get(SHARP_ARGUMENT_CLASS_NAME);
			EngineContext context = getEngineContext();
			// Locate class name, in synonyms map or directly
			targetType = context.getClassSinonyms().get(className);
			if (targetType == null) {
				try {
					targetType = Class.forName(className);
				} catch (ClassNotFoundException e) {
					ActionError error = new ActionError();
					error.setDescription("Class not found for #" + SHARP_ARGUMENT_CLASS_NAME +
							"=" + className);
					errors.add(error);
				}
			}
			// Figure out if there is an empty constructor for the given class
			if (targetType != null) {
				try {
					targetType.getConstructor(new Class<?>[]{});
				} catch (NoSuchMethodException e) {
					ActionError error = new ActionError();
					error.setDescription("No zero argument constructor for class type " + targetType.getName());
					errors.add(error);
				} catch (SecurityException e) {
					ActionError error = new ActionError();
					error.setDescription("No public zero argument constructor for class type " + targetType.getName());
					errors.add(error);
				}
				try {
					targetDomainName = (String) sharpArgumentsMap.get(SHARP_ARGUMENT_DOMAIN);
				} catch (ClassCastException e) {
					ActionError error = new ActionError();
					error.setDescription("#" + SHARP_ARGUMENT_DOMAIN + " if specified must be an String");
					errors.add(error);
				}
			}
		} else {
			ActionError error = new ActionError();
			error.setDescription("A String #className argument is required to build fact");
			errors.add(error);
		}
		return errors;
	}

	public void onExecute(List<Object> conditionMatchingObjects)
			throws ExecutionException {
		// Get target type
		try {
			Constructor<?> constructor = targetType.getConstructor(new Class<?>[]{});
			Object targetObject = constructor.newInstance();
			putArgumentsIntoTargetObject(targetObject, getRegularArgumentsMap());
			EngineContext context = getEngineContext();
			if (targetDomainName == null) {
				context.addFact(targetObject);
			} else {
				context.addFact(targetDomainName, targetObject);
			}
		} catch (org.rulemaker.engine.executors.exception.ExecutionException e) {
			throw e;
		} catch (Exception e) {
			throw new ExecutionException("Exception invoking zero argument constructor, "
					+ " maybe validation might not have been performed before executing rule");
		}
	}

}
