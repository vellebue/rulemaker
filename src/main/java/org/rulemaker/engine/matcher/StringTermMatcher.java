package org.rulemaker.engine.matcher;

import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.rulemaker.engine.matcher.exception.MatchingException;
import org.rulemaker.model.Term;

public class StringTermMatcher extends TermMatcher {

	protected StringTermMatcher(Map<String, Object> globalVariablesMap,
			Term termPattern) {
		super(globalVariablesMap, termPattern);
	}

	@Override
	public boolean matches(Object object) throws MatchingException {
		boolean matches = false;
		String termName = getTermPattern().getIdentifier();
		String termValue = getTermPattern().getExpressionValue();
		try {
			Object propertyValue = PropertyUtils.getProperty(object, termName);
			if (propertyValue instanceof String) {
				matches = termValue.equals(propertyValue);
			} else {
				matches = false;
			}
		} catch (Exception e) {
			// Property not found, no match
			matches = false;
		} 
		return matches;
	}

}
