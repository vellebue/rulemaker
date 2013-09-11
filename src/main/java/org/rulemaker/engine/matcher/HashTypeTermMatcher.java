package org.rulemaker.engine.matcher;

import java.util.Map;

import org.rulemaker.engine.EngineContext;
import org.rulemaker.model.Term;

public class HashTypeTermMatcher extends TermMatcher {
	
	private Map<String, Class<?>> classSinonymsMap;

	protected HashTypeTermMatcher(EngineContext context, Term termPattern) {
		super(context, termPattern);
		classSinonymsMap = context.getClassSinonyms();
	}
	
	protected Map<String, Class<?>> getClassSinonymsMap() {
		return classSinonymsMap;
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
