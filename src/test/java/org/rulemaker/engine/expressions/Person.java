package org.rulemaker.engine.expressions;

public class Person {
	
	private String name;
	private String []registerCodes;
	private double salary;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getRegisterCodes() {
		return registerCodes;
	}

	public void setRegisterCodes(String[] registerCodes) {
		this.registerCodes = registerCodes;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}
}
