package org.rulemaker.engine.matcher;

import java.util.Map;

import org.rulemaker.model.Term;

public class HashTypeTermMatcher extends TermMatcher {
	
	private Map<String, Class<?>> classSinonymsMap;

	protected HashTypeTermMatcher(Map<String, Object> globalVariablesMap, Term termPattern) {
		super(globalVariablesMap, termPattern);
	}
	
	protected Map<String, Class<?>> getClassSinonymsMap() {
		return classSinonymsMap;
	}

	public void setClassSinonymsMap(Map<String, Class<?>> classSinonymsMap) {
		this.classSinonymsMap = classSinonymsMap;
	}

	@Override
	public boolean matches(Object object) {
		Term term = getTermPattern();
		String className = term.getExpressionValue();
		if (classSinonymsMap.get(className) != null) {
			Class<?> classObject = classSinonymsMap.get(className);
			return classObject.isInstance(object);
		} else {
			try {
				Class<?> classObject = Class.forName(className);
				return classObject.isInstance(object);
			} catch (ClassNotFoundException e) {
				return false;
			}
		}
	}

}
