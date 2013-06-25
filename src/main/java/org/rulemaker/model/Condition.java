package org.rulemaker.model;

import java.util.List;

public class Condition extends RuleToken {
		
	public Condition() {
	}
	
	public Condition(List<Term> termsList) {
		super(termsList);
	}	
}
