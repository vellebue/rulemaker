package org.rulemaker.engine.matcher;

import java.util.HashMap;
import java.util.Map;

import org.rulemaker.model.Condition;

class ConditionMatcher {
	
	private Map<String, Object> gobalVariablesMap = new HashMap<String, Object>();
	
	public ConditionMatcher() {
		super();
	}
	
	public Map<String, Object> getGobalVariablesMap() {
		return gobalVariablesMap;
	}

	public void setGobalVariablesMap(Map<String, Object> gobalVariablesMap) {
		this.gobalVariablesMap = gobalVariablesMap;
	}

	public boolean matches(Object object, Condition condition) {
		// TODO Pending
		return false;
	}
}
