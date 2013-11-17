package org.rulemaker.engine.matcher;

import java.util.List;

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
	 * @return A list of matching facts for the rule that has been provided. Notice in that case
	 *         the size of the list will be the same as the number of conditions for the given rule
	 *         and the order of the items in the list will correspond to the conditions in that rule.
	 *         If there is no matching list for the given rule and the given engine context 
	 *         <code>null</code> will be returned.
	 *         
	 * @throws MatchingException If there is a problem matching the rule with a fact. Usually due to
	 *                           an error evaluating an expression.
	 */
	public List<Object> matches(EngineContext engineContext, Rule rule) throws MatchingException;

}
