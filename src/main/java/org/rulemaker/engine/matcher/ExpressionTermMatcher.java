package org.rulemaker.engine.matcher;

import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.rulemaker.engine.expressions.ExpressionSolver;
import org.rulemaker.model.Term;

public class ExpressionTermMatcher extends TermMatcher {
	
	private ExpressionSolver expressionSolver;

	protected ExpressionTermMatcher(ExpressionSolver solver, Map<String, Object> globalVariablesMap,
			Term termPattern) {
		super(globalVariablesMap, termPattern);
		this.expressionSolver = solver;
	}

	@Override
	public boolean matches(Object object) {
		Term term = getTermPattern();
		String identifier = term.getIdentifier();
		try {
			Object objectValue = PropertyUtils.getProperty(object, identifier);
			String expressionPattern = term.getExpressionValue();
			Object expressionValue = expressionSolver.eval(getGlobalVariablesMap(), expressionPattern);
			if (objectValue != null) {
				return objectValue.equals(expressionValue);
			} else {
				return expressionValue == null;
			}			
		} catch (Exception e) {
			return false;
		} 
	}

}
