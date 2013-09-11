package org.rulemaker.engine.matcher;

import org.apache.commons.beanutils.PropertyUtils;
import org.rulemaker.engine.EngineContext;
import org.rulemaker.model.Term;

public class StringTermMatcher extends TermMatcher {

	protected StringTermMatcher(EngineContext context,
			Term termPattern) {
		super(context, termPattern);
	}

	@Override
	public boolean matches(Object object) {
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
