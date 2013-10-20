package org.rulemaker.engine.executors.exception;

/**
 * This exception should be thrown when there is a problem
 * while executing an action.
 * 
 * @author &Aacute;ngel Garc&iacute;a Bastanchuri
 *
 */
public class ExecutionException extends RuntimeException {

	private static final long serialVersionUID = 8775067061204894482L;

	public ExecutionException(String message) {
		super(message);
	}

	public ExecutionException(Throwable cause) {
		super(cause);
	}
	
}