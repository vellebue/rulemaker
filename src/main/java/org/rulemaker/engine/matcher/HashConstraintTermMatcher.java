package org.rulemaker.engine.matcher;

import java.util.Map;

import org.rulemaker.engine.EngineContext;
import org.rulemaker.engine.expressions.ExpressionSolver;
import org.rulemaker.engine.expressions.exception.InvalidExpressionException;
import org.rulemaker.engine.matcher.exception.MatchingException;
import org.rulemaker.model.Term;

public class HashConstraintTermMatcher extends TermMatcher{
	
	private ExpressionSolver expressionSolver;

	protected HashConstraintTermMatcher(ExpressionSolver expressionSolver, Map<String, Object> globlaVariablesMap,
			Term termPattern) {
		super(globlaVariablesMap, termPattern);
		this.expressionSolver = expressionSolver;
	}

	@Override
	public boolean matches(Object object) throws MatchingException{
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
				throw new MatchingException(e);
			}
		} else {
			return false;
		}
	}
}
