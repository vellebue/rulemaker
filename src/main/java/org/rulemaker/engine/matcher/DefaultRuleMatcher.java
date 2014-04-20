package org.rulemaker.engine.matcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.rulemaker.engine.EngineContext;
import org.rulemaker.engine.matcher.exception.MatchingException;
import org.rulemaker.model.Condition;
import org.rulemaker.model.Rule;
import org.rulemaker.model.Term;

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
	
	public Map<String, Object> matches(EngineContext engineContext, Rule rule) throws MatchingException {
		// TODO Auto-generated method stub
		return matches(engineContext, new HashMap<String, Object>(), rule, 1);
	}

	private Map<String, Object> matches(EngineContext engineContext, Map<String, Object> contextMap, Rule rule, int conditionIndex) throws MatchingException {
		if (rule.getConditionList().size() == 0) {
			// If there is no conditions matching is OK
			// with 0 objects matched
			/*
			return new ArrayList<Object>();
			*/
			return contextMap;
		} else {
			// There is at least one condition, find and object that satisfies it
			// and then figure out if there are more objects that satisfy the
			// remaining conditions
			//List<Object> matchingObjectsList = null;
			Map<String, Object> resultingMap = null;
			Condition headCondition = removeHeadingConditionFromRule(rule);		
			ConditionMatcher matcher = new ConditionMatcher();
			matcher.setEngineContext(engineContext);
			String domainName = extractDomainNameFromCondition(headCondition);
			List<Object> factBaseObjects = engineContext.getFactBase().get(domainName);
			Iterator<Object> factBaseObjectsIterator = factBaseObjects.iterator();
			//boolean foundMatching = false;
			while ((resultingMap == null) && factBaseObjectsIterator.hasNext()) {
				Object currentCandidateObject = factBaseObjectsIterator.next();
				Map<String, Object> currentCandidateObjectMap = 
						new ChainedMap<String, Object>(new HashMap<String, Object>(), 
								new ChainedMap<String, Object>(contextMap));
				if (matcher.matches(currentCandidateObject, currentCandidateObjectMap, headCondition)) {
					// put object with index _i in context map
					currentCandidateObjectMap.put("_" + conditionIndex, currentCandidateObject);
					resultingMap = matches(engineContext,  currentCandidateObjectMap,rule, conditionIndex + 1);
				}
				/*
				List<String> previousVariableNames = getGlobalVariableNamesAsList(engineContext.getGobalVariablesMap());
				if (matcher.matches(currentCandidateObject, headCondition)) {
					List<String> variableNamesAferMatching = getGlobalVariableNamesAsList(engineContext.getGobalVariablesMap());
					Map<String, Object> currentGlobalVariablesMap = engineContext.getGobalVariablesMap();
					currentGlobalVariablesMap.put("_" + conditionIndex, currentCandidateObject);
					matchingObjectsList = matches(engineContext, rule, conditionIndex + 1);
					currentGlobalVariablesMap.remove("_" + conditionIndex);
					if (matchingObjectsList != null) {
						matchingObjectsList.add(0, currentCandidateObject);
						foundMatching = true;
					} else {
						// Due its going to change matching for current condition
						// variables added after condition matching are no longer needed
						removeAddedVariableNamesAfterMatchingFromMap(previousVariableNames, variableNamesAferMatching, 
								engineContext.getGobalVariablesMap());
					}
				}
				*/
			}
			rule.getConditionList().add(0, headCondition);
			/*
			if (foundMatching) {
				return matchingObjectsList;
			} else {
				return null;
			}
			*/
			return resultingMap;
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
	
	private List<String> getGlobalVariableNamesAsList(Map<String, Object> variablesMap) {
		List<String> variableNamesList = new ArrayList<String>();
		for (String aVariableName : variablesMap.keySet()) {
			variableNamesList.add(aVariableName);
		}
		return variableNamesList;
	}
	
	private void removeAddedVariableNamesAfterMatchingFromMap(List<String> previousVariableNames, 
			                                     List<String> variableNamesAfterMatching,
			                                     Map<String, Object> variablesMap) {
		// First find new variable names added
		List<String> newVariableNames = new ArrayList<String>();
		for (String aName : variableNamesAfterMatching) {
			boolean found = false;
			Iterator<String> previousVariableNamesIterator = previousVariableNames.iterator();
			while(!found && previousVariableNamesIterator.hasNext()) {
				String aPreviousName = previousVariableNamesIterator.next();
				found = aPreviousName.equals(aName);
			}
			if (!found) {
				newVariableNames.add(aName);
			}
		}
		// Then remove them from map
		for (String aName :newVariableNames) {
			variablesMap.remove(aName);
		}
	}
	
	private String extractDomainNameFromCondition(Condition condition) {
		Iterator<Term> termIterator = condition.getTermsList().iterator();
		String domainName = null;
		while ((domainName == null) && (termIterator.hasNext())) {
			Term currentTerm = termIterator.next();
			if (currentTerm.getIdentifier().equals(DefaultRuleMatcher.FACT_DOMAIN) &&
			    currentTerm.isSharpTerm()) {
				domainName = currentTerm.getExpressionValue();
			}
		}
		if (domainName == null) {
			domainName = EngineContext.DEFAULT_DOMAIN_LIST_NAME;
		}
		return domainName;
	}
}
