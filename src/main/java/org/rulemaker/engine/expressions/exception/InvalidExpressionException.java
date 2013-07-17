package org.rulemaker.engine.expressions.exception;

public class InvalidExpressionException extends Exception{

	private static final long serialVersionUID = -4701287477160427716L;

	public InvalidExpressionException() {
		super();
	}

	public InvalidExpressionException(String message) {
		super(message);
	}

	public InvalidExpressionException(Throwable cause) {
		super(cause);
	}

}
