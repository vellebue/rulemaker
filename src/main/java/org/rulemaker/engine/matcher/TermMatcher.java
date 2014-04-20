package org.rulemaker.engine.matcher;

import java.util.Map;

import org.rulemaker.engine.EngineContext;
import org.rulemaker.engine.expressions.OgnlExpressionSolver;
import org.rulemaker.engine.matcher.exception.MatchingException;
import org.rulemaker.model.Term;

abstract class TermMatcher {
	
	public static class Factory {
		
		public static TermMatcher buildTermMatcher(EngineContext context, Map<String, Object> globalVariablesMap,
				Term term) {
			if (term.isSharpTerm()) {
				String termName = term.getIdentifier();
				if (termName.equals(DefaultRuleMatcher.FACT_CONSTRAINT)) {
					return new HashConstraintTermMatcher(new OgnlExpressionSolver(), globalVariablesMap, term);
				} 
				else if(termName.equals(DefaultRuleMatcher.CONDITION_CLASS_TYPE)) {
					return new HashTypeTermMatcher(context, globalVariablesMap, term);
				} 
				else if (termName.equals(DefaultRuleMatcher.FACT_DOMAIN)) {
					// Return a neutral term due this term must be processed before 
					return new HashConstraintTermMatcher(new OgnlExpressionSolver(), globalVariablesMap, new Term(DefaultRuleMatcher.FACT_CONSTRAINT, Term.TermType.EXPRESSION, "true", true));
				} else {
					return null;
				}
				
			} else if (term.getExpressionType().equals(Term.TermType.STRING)) {
				return new StringTermMatcher(globalVariablesMap, term);
			} else if (term.getExpressionType().equals(Term.TermType.IDENTIFIER)) {
				return new IdentifierTermMatcher(globalVariablesMap, term);
			} else if (term.getExpressionType().equals(Term.TermType.NUMBER)) {
				return new NumberTermMatcher(globalVariablesMap, term);
			} else if (term.getExpressionType().equals(Term.TermType.EXPRESSION)) {
				return new ExpressionTermMatcher(new OgnlExpressionSolver(), globalVariablesMap, term);
			} else {
				return null;
			}
		}
	}
	
	private Term termPattern;
	private Map<String, Object> globalVariablesMap;
	
	protected TermMatcher(Map<String, Object> globalVariablesMap, Term termPattern) {
		this.termPattern = termPattern;
		this.globalVariablesMap = globalVariablesMap;
	}
	
	protected Term getTermPattern() {
		return termPattern;
	}

	protected Map<String, Object> getGlobalVariablesMap() {
		return globalVariablesMap;
	}

	public abstract boolean matches(Object object) throws MatchingException;
	
}
