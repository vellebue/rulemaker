package org.rulemaker.engine.matcher.exception;

/**
 * Exception thrown when there is a problem matching rules
 * with facts.
 * 
 * @author &Aacute;ngel Garc&iacute;a Bastanchuri
 *
 */
public class MatchingException extends RuntimeException {

	private static final long serialVersionUID = -2406917240909515166L;

	public MatchingException(String message) {
		super(message);
	}

	public MatchingException(Throwable cause) {
		super(cause);
	}
}
