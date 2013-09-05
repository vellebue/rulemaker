package org.rulemaker.engine.matcher;

import java.util.Map;

import org.rulemaker.engine.expressions.ExpressionSolver;
import org.rulemaker.engine.expressions.exception.InvalidExpressionException;
import org.rulemaker.model.Term;

public class HashConstraintTermMatcher extends TermMatcher{
	
	private ExpressionSolver expressionSolver;

	protected HashConstraintTermMatcher(ExpressionSolver expressionSolver, Map<String, Object> globalVariablesMap,
			Term termPattern) {
		super(globalVariablesMap, termPattern);
		this.expressionSolver = expressionSolver;
	}

	@Override
	public boolean matches(Object object) {
		Term term = getTermPattern();
		if (term.getExpressionType().equals(Term.TermType.EXPRESSION)) {
			try {
				Object value = this.expressionSolver.eval(getGlobalVariablesMap(), term.getExpressionValue());
				if (value instanceof Boolean) {
					return (Boolean) value;
				} else {
					return value != null;
				}
			} catch (InvalidExpressionException e) {
				throw new RuntimeException(e);
			}
		} else {
			return false;
		}
	}
}
