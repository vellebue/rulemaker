package org.rulemaker.engine;

/**
 * Interface that defines common methods to operate a 
 * rules engine.
 * 
 * @author &Aacute;ngel Garc&iacute;a Bastanchuri
 *
 */
public interface Engine {
	
	/**
	 * Executes one step running engine (i.e. finds a rule
	 * to apply, applies that rule and executes it).
	 * 
	 * @return true if it was possible to perform a rule step
	 *         false otherwise.
	 * 
	 * @throws EngineException If an exception is found
	 *         during rule execution.
	 */
	public boolean runStep() throws EngineException;
	
	/**
	 * Executes all steps possible until there is no chance to
	 * execute more steps.
	 * 
	 * @throws EngineException If an exception is found during
	 *                         an step execution.
	 */
	public void runEngine() throws EngineException;

}
