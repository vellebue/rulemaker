package org.rulemaker.engine.actionengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.rulemaker.engine.EngineContext;
import org.rulemaker.engine.EngineException;
import org.rulemaker.engine.executors.ActionError;
import org.rulemaker.engine.executors.BaseActionExecutor;
import org.rulemaker.engine.executors.exception.ExecutionException;
import org.rulemaker.engine.expressions.ExpressionSolver;
import org.rulemaker.engine.expressions.OgnlExpressionSolver;
import org.rulemaker.engine.expressions.exception.InvalidExpressionException;
import org.rulemaker.model.Action;
import org.rulemaker.model.Term;

public class BaseActionEngine implements ActionEngine{
	
	private Map<String, Class<? extends BaseActionExecutor>> actionExecutorsMap =
			new HashMap<String, Class<? extends BaseActionExecutor>>();
	private ExpressionSolver expressionSolver = new OgnlExpressionSolver();
	private EngineContext context;
	
	public void addExecutorClass(String executorName, Class<? extends BaseActionExecutor> executorClass) {
		actionExecutorsMap.put(executorName, executorClass);
	}

	public void executeAction(Map<String, Object> matchingConditionVariables,
			Action ruleAction) throws EngineException {
		Map<String, Object>[]maps = getSharpAndRegularValuesMaps(matchingConditionVariables, ruleAction);
		Map<String, Object> sharpArgumentsMap = maps[0];
		Map<String, Object> regularArgumentsMap = maps[1];
		String actionName = ruleAction.getActionName();
		Class<? extends BaseActionExecutor> actionClassName = actionExecutorsMap.get(actionName);
		if (actionClassName != null) {
			try {
				BaseActionExecutor executor = actionClassName.newInstance();
				executor.setEngineContext(context);
				List<ActionError> actionErrors = executor.validate(sharpArgumentsMap, regularArgumentsMap);
				if ((actionErrors != null) && (actionErrors.size() > 0)) {
					//notify errors throwing exception 
					throw buildEngineExceptionFromActionErrors(actionErrors);
				} else {
					executor.execute(buildMatchingObjectsListFromMap(matchingConditionVariables));
				}
			} catch (Exception e) {
				throw new EngineException(e);
			} 
		} else {
			throw new EngineException("Action executor type not found: " + actionName);
		}
	}

	/**
	 * Gets the expression solver used by this action engine.
	 * 
	 * @return the expression solver instance used by this action engine.
	 */
	protected ExpressionSolver getExpressionSolver() {
		return expressionSolver;
	}
	
	public void setEngineContext(EngineContext context) {
		this.context = context;
	}

	public EngineContext getEngineContext() {
		return context;
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object>[] getSharpAndRegularValuesMaps(Map<String, Object> matchingConditionVariables,
			Action ruleAction) throws EngineException {
		Map<String, Object> sharpValuesMap = new HashMap<String, Object>();
		Map<String, Object> regularValuesMap = new HashMap<String, Object>();
		for (Term actionTerm : ruleAction.getTermsList()) {
			String termName = actionTerm.getIdentifier();
			Object termValue = solveActionTermValue(matchingConditionVariables, actionTerm);
			if (actionTerm.isSharpTerm()) {
				sharpValuesMap.put(termName, termValue);
			} else {
				regularValuesMap.put(termName, termValue);
			}
		}
		return new  Map[]{sharpValuesMap, regularValuesMap};
	}
	
	private Object solveActionTermValue(Map<String, Object> matchingConditionVariables, 
			                            Term actionTerm) throws EngineException {
		Term.TermType actionTermType = actionTerm.getExpressionType();
		String stringValue = actionTerm.getExpressionValue();
		Object value = null;
		if (actionTermType.equals(Term.TermType.NUMBER)) {
			// First parse it as an integer
			try {
				value = Integer.parseInt(stringValue);
			} catch (NumberFormatException e) {
				try {
					// Then parse it as a double value
					value = Double.parseDouble(stringValue);
				} catch (Exception e2) {
					throw new EngineException("Invalid value as number value: " + stringValue);
				}
			}
		} else if (actionTermType.equals(Term.TermType.STRING)) {
			value = stringValue;
		} else if (actionTermType.equals(Term.TermType.IDENTIFIER)) {
			throw new EngineException("Identifiers are not allowed in action terms");
		} else if (actionTermType.equals(Term.TermType.EXPRESSION)) {
			try {
				value = getExpressionSolver().eval(matchingConditionVariables, stringValue);
			} catch (InvalidExpressionException e) {
				throw new EngineException(e);
			}
		} else {
			throw new EngineException("Unssuported term type resolving term value: " + actionTermType);
		}
		return value;
	}
	
	private EngineException buildEngineExceptionFromActionErrors(List<ActionError> errors) {
		StringBuilder errorDescription = new StringBuilder("Errors detected validating action:\n");
		int i = 1;
		for (ActionError error :errors) {
			errorDescription.append(i + ". " + error.getDescription() + "\n");
			i++;
		}
		return new EngineException(errorDescription.toString());
	}

	private List<Object> buildMatchingObjectsListFromMap(Map<String, Object> matchingConditionVariables) {
		// First extract matching objects from map
		Map<String, Object> resultMap = new HashMap<String, Object>();
		for (String aVariableName : matchingConditionVariables.keySet()) {
			if (aVariableName.startsWith("_")) {
				resultMap.put(aVariableName, matchingConditionVariables.get(aVariableName));
			}
		}
		// Then build the list in the right order
		List<Object> matchingObjectsList  = new ArrayList<Object>();
		for (int i = 0 ; i < resultMap.size() ; i++) {
			matchingObjectsList.add(resultMap.get("_" + (i + 1)));
		}
		return matchingObjectsList;
	}
}
