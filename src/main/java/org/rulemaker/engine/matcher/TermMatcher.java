package org.rulemaker.engine.matcher;

import java.util.Map;

import org.rulemaker.engine.EngineContext;
import org.rulemaker.engine.expressions.OgnlExpressionSolver;
import org.rulemaker.model.Term;

abstract class TermMatcher {
	
	public static class Factory {
		
		public static TermMatcher buildTermMatcher(EngineContext context,
				Term term) {
			if (term.isSharpTerm()) {
				String termName = term.getIdentifier();
				if (termName.equals(DefaultRuleMatcher.FACT_CONSTRAINT)) {
					return new HashConstraintTermMatcher(new OgnlExpressionSolver(), context, term);
				} 
				else if(termName.equals(DefaultRuleMatcher.CONDITION_CLASS_TYPE)) {
					return new HashTypeTermMatcher(context, term);
				} 
				else if (termName.equals(DefaultRuleMatcher.FACT_DOMAIN)) {
					// Return a neutral term due this term must be processed before 
					return new HashConstraintTermMatcher(new OgnlExpressionSolver(), context, new Term(DefaultRuleMatcher.FACT_CONSTRAINT, Term.TermType.EXPRESSION, "true", true));
				} else {
					return null;
				}
				
			} else if (term.getExpressionType().equals(Term.TermType.STRING)) {
				return new StringTermMatcher(context, term);
			} else if (term.getExpressionType().equals(Term.TermType.IDENTIFIER)) {
				return new IdentifierTermMatcher(context, term);
			} else if (term.getExpressionType().equals(Term.TermType.NUMBER)) {
				return new NumberTermMatcher(context, term);
			} else if (term.getExpressionType().equals(Term.TermType.EXPRESSION)) {
				return new ExpressionTermMatcher(new OgnlExpressionSolver(), context, term);
			} else {
				return null;
			}
		}
	}
	
	private Term termPattern;
	private Map<String, Object> globalVariablesMap;
	
	protected TermMatcher(EngineContext engineContext, Term termPattern) {
		this.termPattern = termPattern;
		this.globalVariablesMap = engineContext.getGobalVariablesMap();
	}
	
	protected Term getTermPattern() {
		return termPattern;
	}

	protected Map<String, Object> getGlobalVariablesMap() {
		return globalVariablesMap;
	}

	public abstract boolean matches(Object object);
	
}
