package org.rulemaker.engine;

import java.util.List;

import org.rulemaker.model.Rule;

class TestEngine extends BaseEngine {
	
	public TestEngine(String rulesText) throws EngineException {
		super(rulesText);
		// TODO Auto-generated constructor stub
	}

	public TestEngine(List<Rule> rules) {
		super(rules);
		// TODO Auto-generated constructor stub
	}	
}
