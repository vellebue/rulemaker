package org.rulemaker.model;

import java.util.List;

public class Action extends RuleToken {
	
	private String actionName;
	
	public Action() {
	}
	
	public Action(String actionName, List<Term> termList) {
		super(termList);
		this.actionName = actionName;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	@Override
	public int hashCode() {
		return 37*super.hashCode() + 
				((actionName != null) ? actionName.hashCode() : 0);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Action) {
			Action action = (Action) obj;
			return super.equals(action) &&
					((actionName != null) ? actionName.equals(action.getActionName()) : action.getActionName() == null);
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return actionName + super.toString();
	}
	
	
}
