package org.rulemaker.engine.matcher;

import java.util.Map;

import org.rulemaker.engine.EngineContext;
import org.rulemaker.engine.matcher.exception.MatchingException;
import org.rulemaker.model.Rule;

/**
 * A rule matcher is a component that verifies if there is
 * a facts set that matches the conditions for a given rule.
 * 
 * @author &Aacute;ngel Garc&iacute;a Bastanchuri
 * 
 *
 */
public interface RuleMatcher {
	
	/**
	 * Figures out if there is an object list that matches
	 * the conditions from the given rule.
	 * 
	 * @param engineContext An engine context that provides a whole set of
	 * 					    object facts organized into domains (see {@link EngineContext}).
	 * 
	 * @param rule A rule whose condition list will be examined looking for matching facts from
	 * 			   the engine context.
	 * 
	 * @return A map containing matching facts and matching variable values or 
	 *         <code>null</code> if there is no matching facts set for this rule.
	 *         Notice that matching facts can be found in this map stored under names
	 *         "_1", "_2" ... "_n" once assumed this rule has n conditions. Variable values used
	 *         to match conditions are also stored providing its values.
	 *         
	 * @throws MatchingException If there is a problem matching the rule with a fact. Usually due to
	 *                           an error evaluating an expression.
	 */
	public Map<String, Object> matches(EngineContext engineContext, Rule rule) throws MatchingException;

}
