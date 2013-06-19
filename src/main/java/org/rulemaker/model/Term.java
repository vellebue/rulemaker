package org.rulemaker.model;

public class Term {
	
	public enum TermType {NUMBER, STRING, IDENTIFIER};
	
	private String identifier;
	private TermType expressionType;
	private String expressionValue;
	
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
}
