package org.rulemaker.model;

import java.util.ArrayList;
import java.util.List;

public abstract class RuleToken {
	
	private List<Term> termsList = new ArrayList<Term>();
	
	public RuleToken() {
	}
	
	public RuleToken(List<Term> termsList) {
		super();
		this.termsList = termsList;
	}
	
	public void addTerm(Term term) {
		termsList.add(term);
	}

	public List<Term> getTermsList() {
		return termsList;
	}

	@Override
	public int hashCode() {
		return this.termsList.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RuleToken) {
			RuleToken ruleToken = (RuleToken) obj;
			return this.termsList.equals(ruleToken.getTermsList());
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("(\n");
		for (Term term : termsList) {
			buffer.append(term.toString() + '\n');
		}
		buffer.append(")");
		return buffer.toString();
	}

}
