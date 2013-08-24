package org.rulemaker.engine.matcher;

import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.rulemaker.model.Term;

public class NumberTermMatcher extends TermMatcher {

	protected NumberTermMatcher(Map<String, Object> globalVariablesMap,
			Term termPattern) {
		super(globalVariablesMap, termPattern);
	}

	@Override
	public boolean matches(Object object) {
		Term term = getTermPattern();
		String identifier = term.getIdentifier();
		try {
			Object value = PropertyUtils.getProperty(object, identifier);
			String numberTermValue = term.getExpressionValue();
			if (value instanceof Integer) {
				try {
					Integer integerTermValue = Integer.parseInt(numberTermValue);
					return integerTermValue.equals(value);
				} catch (NumberFormatException e) {
					return false;
				}
			} else if (value instanceof Double){
				Double doubleTermValue = Double.parseDouble(numberTermValue);
				return doubleTermValue.equals(value);
			} else if (value instanceof Float){
				Float floatTermValue = Float.parseFloat(numberTermValue);
				return floatTermValue.equals(value);
			} else {
				return false;
			}
			
		} catch (Exception e) {
			return false;
		} 
	}

}
