package org.rulemaker.engine.executors;

/**
 * Describes an error while validating arguments for an
 * action rule before executing it. 
 * 
 * @author &Aacute;ngel Garc&iacute;a Bastanchuri
 *
 */
public class ActionError {
	
	private String description;

	/**
	 * Gets the description for an action error.
	 * 
	 * @return the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description for an action error.
	 * 
	 * @param description The action error description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
