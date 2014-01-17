package org.rulemaker.engine;

public class EngineException extends Exception {

	private static final long serialVersionUID = 2553873031470743847L;

	public EngineException(String message) {
		super(message);
	}

	public EngineException(Throwable cause) {
		super(cause);
	}
}
