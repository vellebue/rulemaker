package org.rulemaker.engine.matcher;

import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.rulemaker.engine.matcher.exception.MatchingException;
import org.rulemaker.model.Term;

public class IdentifierTermMatcher extends TermMatcher {

	protected IdentifierTermMatcher(Map<String, Object> globalVariablesMap,
			Term termPattern) {
		super(globalVariablesMap, termPattern);
	}

	@Override
	public boolean matches(Object object) throws MatchingException {
		Term term = getTermPattern();
		String termName = term.getIdentifier();
		try {
			Object objectValue = PropertyUtils.getProperty(object, termName);
			String variableName = term.getExpressionValue();
			Object variableValue = getGlobalVariablesMap().get(variableName);
			if (variableValue != null) {
				return variableValue.equals(objectValue);
			} else {
				getGlobalVariablesMap().put(variableName, objectValue);
				return true;
			}
		} catch (Exception e) {
			//No matching property
			return false;
		} 
	}

}
