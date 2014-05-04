package org.rulemaker.engine.actionengine;

import java.util.Map;

import org.rulemaker.engine.EngineContext;
import org.rulemaker.engine.EngineException;
import org.rulemaker.engine.executors.BaseActionExecutor;
import org.rulemaker.model.Action;

/**
 * Common interface for action engines. Action engines must execute actions
 * once a rule is matched and its conditions variables have beedn resolved.
 * 
 * @author &Aacute;ngel Garc&iacute;a Bastanchuri
 *
 */
public interface ActionEngine {
	
	/**
	 * Adds an executor class type to this action engine.
	 * 
	 * @param executorName The name of the action as it is identified in the action
	 *                     clause in rules.
	 *                      
	 * @param executorClass The class implementing the action executor to be performed
	 *                      once the action is fired.
	 */
	public void addExecutorClass(String executorName, 
			                     Class<? extends BaseActionExecutor> executorClass);
	
	/**
	 * Executes an action given a matching condition variables map
	 * and the action definition from the rule.
	 * 
	 * @param matchingConditionVariables The map containing the conditions definitions.
	 *                                   Condition facts are referred as <code>_1</code>, 
	 *                                   <code>_2</code>... and somoe other variables are
	 *                                   referred by their corresponding names.
	 *                                    
	 * @param ruleAction A rule action that must be executed resolving its arguments from
	 * 					 the matching condition variables.
	 * 
	 * @throws EngineException If action executor is not found or there are problems 
	 *                         executing it.
	 */
	public void executeAction(Map<String, Object> matchingConditionVariables, 
			                  Action ruleAction) throws EngineException;
	
	/**
	 * Sets the engine context.
	 * 
	 * @param context the engine context.
	 */
	public void setEngineContext(EngineContext context);
	
	/**
	 * Gets the engine context.
	 * 
	 * @return the engine context.
	 */
	public EngineContext getEngineContext();

}
