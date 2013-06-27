package org.rulemaker.model;

import java.util.List;

public class Rule {
	
	private List<Condition> conditionList;
	private List<Action> actionList;
	
	public Rule() {
		super();
	}

	public Rule(List<Condition> conditionList, List<Action> actionList) {
		super();
		this.conditionList = conditionList;
		this.actionList = actionList;
	}

	public List<Condition> getConditionList() {
		return conditionList;
	}
	
	public void setConditionList(List<Condition> conditionList) {
		this.conditionList = conditionList;
	}
	
	public List<Action> getActionList() {
		return actionList;
	}
	
	public void setActionList(List<Action> actionList) {
		this.actionList = actionList;
	}

	@Override
	public int hashCode() {
		int hash = (conditionList != null) ? conditionList.hashCode() : 0;
		hash = 37 * hash +
				   ((actionList != null) ? actionList.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Rule) {
			Rule rule = (Rule) obj;
			return ((conditionList != null) ? 
					     conditionList.equals(rule.getConditionList()) : 
					    	 rule.getConditionList() == null) &&
				   ((actionList != null) ? 
						  actionList.equals(rule.getActionList()) : 
							 rule.getActionList() == null ) ;
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return conditionList.toString() +
				"\n->\n" +
			   actionList.toString();
	}
}
