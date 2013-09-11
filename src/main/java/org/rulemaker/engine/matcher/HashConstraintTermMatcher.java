package org.rulemaker.engine.matcher;

import org.rulemaker.engine.EngineContext;
import org.rulemaker.engine.expressions.ExpressionSolver;
import org.rulemaker.engine.expressions.exception.InvalidExpressionException;
import org.rulemaker.model.Term;

public class HashConstraintTermMatcher extends TermMatcher{
	
	private ExpressionSolver expressionSolver;

	protected HashConstraintTermMatcher(ExpressionSolver expressionSolver, EngineContext context,
			Term termPattern) {
		super(context, termPattern);
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
