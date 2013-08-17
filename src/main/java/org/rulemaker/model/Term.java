package org.rulemaker.model;

public class Term {
	
	public enum TermType {NUMBER, STRING, IDENTIFIER, EXPRESSION};
	
	private String identifier;
	private TermType expressionType;
	private String expressionValue;
	private boolean sharpTerm = false;
	
	public Term() {
	}
	
	public Term(String identifier, TermType expressionType,
			String expressionValue) {
		super();
		this.identifier = identifier;
		this.expressionType = expressionType;
		this.expressionValue = expressionValue;
	}
	
	public Term(String identifier, TermType expressionType,
			String expressionValue, boolean sharpTerm) {
		this(identifier, expressionType, expressionValue);
		this.sharpTerm = sharpTerm;
	}

	public String getIdentifier() {
		return identifier;
	}
	
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	public TermType getExpressionType() {
		return expressionType;
	}
	
	public void setExpressionType(TermType expressionType) {
		this.expressionType = expressionType;
	}
	
	public String getExpressionValue() {
		return expressionValue;
	}
	
	public void setExpressionValue(String expressionValue) {
		this.expressionValue = expressionValue;
	}

	public boolean isSharpTerm() {
		return sharpTerm;
	}

	public void setSharpTerm(boolean sharpTerm) {
		this.sharpTerm = sharpTerm;
	}

	@Override
	public int hashCode() {
		int hashCode = (this.identifier != null) ? this.identifier.hashCode() : 0;
		hashCode = 37*hashCode + ((this.expressionType != null) ? this.expressionType.hashCode() : 0);
		hashCode = 37*hashCode + ((this.expressionValue != null) ? this.expressionValue.hashCode() : 0);
		hashCode = 37*hashCode + (this.sharpTerm ? 1 : 0);
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Term) {
			Term term = (Term) obj;
			return ((this.identifier != null) ? this.identifier.equals(term.getIdentifier()) : (term.getIdentifier() == null)) &&
				   ((this.expressionType != null) ? this.expressionType.equals(term.getExpressionType()) : (term.getExpressionType() == null)) &&
				   ((this.expressionValue != null) ? this.expressionValue.equals(term.getExpressionValue()) : (term.getExpressionValue() == null)) &&
				   this.sharpTerm == term.isSharpTerm();
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Term{");
		buffer.append("identifier: " + this.identifier + ", ");
		buffer.append("expressionType: " + this.expressionType + ", ");
		buffer.append("expressionValue: " + this.expressionValue);
		buffer.append("isSharpTerm: " + this.sharpTerm);
		buffer.append("}");
		return buffer.toString();
	}
}
