package org.rulemaker.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.rulemaker.model.Rule;

public class EngineContext {
	
	public static final String DEFAULT_RULE_LIST_NAME = "$default"; 
	
	private List<Rule> rulesList;
	private Map<String, List<Object>> factBase = new HashMap<String, List<Object>>();
	
	private EngineContext() {
		super();
		factBase.put(DEFAULT_RULE_LIST_NAME, new ArrayList<Object>());
	}
	
	public EngineContext(List<Rule> rulesList) {
		this();
		this.rulesList = rulesList;
	}
	
	public List<Rule> getRulesList() {
		return rulesList;
	}

	public Map<String, List<Object>> getFactBase() {
		return factBase;
	}
	
	public List<Object> getFactList() {
		return factBase.get(DEFAULT_RULE_LIST_NAME);
	}
	
	public void addFact(String ruleListName, Object fact) {
		List<Object> factList = factBase.get(ruleListName);
		if (factList == null) {
			factList = new ArrayList<Object>();
			factBase.put(ruleListName, factList);
		}
		factList.add(fact);
	}

	public void addFact(Object fact) {
		addFact(DEFAULT_RULE_LIST_NAME, fact);
	}
}
