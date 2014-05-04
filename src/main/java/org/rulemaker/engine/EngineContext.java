package org.rulemaker.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.rulemaker.model.Rule;

public final class EngineContext {
	
	public static final String DEFAULT_DOMAIN_LIST_NAME = "$default"; 
	
	private List<Rule> rulesList;
	private Map<String, List<Object>> factBase = new HashMap<String, List<Object>>();
	private Map<String, Class<?>> classSinonyms = new HashMap<String, Class<?>>();
	private Map<String, Object> gobalVariablesMap = new HashMap<String, Object>();
	
	private EngineContext() {
		super();
		factBase.put(DEFAULT_DOMAIN_LIST_NAME, new ArrayList<Object>());
	}
	
	public EngineContext(List<Rule> rulesList) {
		this();
		this.rulesList = rulesList;
	}
	
	public void registerClass(String classNameSinonym, Class<?> clazz) {
		classSinonyms.put(classNameSinonym, clazz);
	}
	
	public Map<String, Class<?>> getClassSinonyms() {
		return classSinonyms;
	}

	public List<Rule> getRulesList() {
		return rulesList;
	}

	public Map<String, List<Object>> getFactBase() {
		return factBase;
	}
	
	public List<Object> getFactList() {
		return factBase.get(DEFAULT_DOMAIN_LIST_NAME);
	}
	
	public void addFact(String factDomain, Object fact) {
		List<Object> factList = factBase.get(factDomain);
		if (factList == null) {
			factList = new ArrayList<Object>();
			factBase.put(factDomain, factList);
		}
		factList.add(fact);
	}

	public void addFact(Object fact) {
		addFact(DEFAULT_DOMAIN_LIST_NAME, fact);
	}

	public Map<String, Object> getGobalVariablesMap() {
		return gobalVariablesMap;
	}

	public void setGobalVariablesMap(Map<String, Object> gobalVariablesMap) {
		this.gobalVariablesMap = gobalVariablesMap;
	}
}
