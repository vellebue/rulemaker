package org.rulemaker.model;

import java.util.ArrayList;
import java.util.List;

public class Condition {
	
	private List<Term> conditionTermsList = new ArrayList<Term>();
	
	public Condition() {
	}
	
	public Condition(List<Term> conditionTermsList) {
		super();
		this.conditionTermsList = conditionTermsList;
	}



	public void addTerm(Term term) {
		conditionTermsList.add(term);
	}

	public List<Term> getConditionTermsList() {
		return conditionTermsList;
	}

	@Override
	public int hashCode() {
		return this.conditionTermsList.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Condition) {
			Condition condition = (Condition) obj;
			return this.conditionTermsList.equals(condition.getConditionTermsList());
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("Condition(\n");
		for (Term term : conditionTermsList) {
			buffer.append(term.toString() + '\n');
		}
		buffer.append(")");
		return buffer.toString();
	}
}
