package org.rulemaker.engine.matcher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.rulemaker.engine.EngineContext;
import org.rulemaker.model.Condition;
import org.rulemaker.model.Rule;

/**
 * Default implementation for interface RuleMatcher.
 * 
 * @author &Aacute;ngel Garc&iacute;a Bastanchuri
 *
 */
public class DefaultRuleMatcher implements RuleMatcher {
	
	public static final String CONDITION_CLASS_TYPE = "type";
	public static final String FACT_DOMAIN = "domain";
	public static final String FACT_CONSTRAINT = "constraint";

	public List<Object> matches(EngineContext engineContext, Rule rule) {
		if (rule.getConditionList().size() == 0) {
			// If there is no conditions matching is OK
			// with 0 objects matched
			return new ArrayList<Object>();
		} else {
			// There is at least one condition, find and object that satisfies it
			// and then figure out if there are more objects that satisfy the
			// remaining conditions
			List<Object> matchingObjectsList = null;
			Condition headCondition = removeHeadingConditionFromRule(rule);		
			ConditionMatcher matcher = new ConditionMatcher();
			matcher.setEngineContext(engineContext);
			List<Object> factBaseObjects = engineContext.getFactList();
			Iterator<Object> factBaseObjectsIterator = factBaseObjects.iterator();
			boolean foundMatching = false;
			while (!foundMatching && factBaseObjectsIterator.hasNext()) {
				Object currentCandidateObject = factBaseObjectsIterator.next();
				if (matcher.matches(currentCandidateObject, headCondition)) {
					matchingObjectsList = matches(engineContext, rule);
					if (matchingObjectsList != null) {
						matchingObjectsList.add(0, currentCandidateObject);
						foundMatching = true;
					}
				}
			}
			rule.getConditionList().add(0, headCondition);
			if (foundMatching) {
				return matchingObjectsList;
			} else {
				return null;
			}			
		}
	}
	
	private Condition removeHeadingConditionFromRule(Rule rule) {
		List<Condition> remaininConditions = new ArrayList<Condition>();
		Condition headingCondition = null;
		for (int i = 0 ; i < rule.getConditionList().size() ; i++) {
			Condition currentCondition = rule.getConditionList().get(i);
			if (i == 0) {
				headingCondition = currentCondition;
			} else {
				remaininConditions.add(currentCondition);
			}
		}
		rule.setConditionList(remaininConditions);
		return headingCondition;
	}

}
