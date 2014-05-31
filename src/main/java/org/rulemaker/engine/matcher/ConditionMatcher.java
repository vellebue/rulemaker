package org.rulemaker.engine.matcher;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.rulemaker.engine.EngineContext;
import org.rulemaker.engine.matcher.exception.MatchingException;
import org.rulemaker.model.Condition;
import org.rulemaker.model.Term;


class ConditionMatcher {
	
	private EngineContext engineContext; 
	
	public ConditionMatcher() {
		super();
	}
	
	protected EngineContext getEngineContext() {
		return engineContext;
	}

	public void setEngineContext(EngineContext engineContext) {
		this.engineContext = engineContext;
	}

	public boolean matches(Object object, Map<String, Object> globalVariablesMap, Condition condition) throws MatchingException {
		Map<String, Object> objectMembersMap = buildMapFromObjectMembers(object);
		ChainedMap<String, Object> globalMap = 
				new ChainedMap<String, Object>(globalVariablesMap, 
						new ChainedMap<String, Object>(objectMembersMap));
		boolean matches = true;
		List<Term> conditionTerms = condition.getTermsList();
		Iterator<Term> iterator = conditionTerms.iterator();
		while(matches && iterator.hasNext()) {
			Term currentTerm = iterator.next();
			TermMatcher termMatcher = TermMatcher.Factory.buildTermMatcher(engineContext, globalMap , currentTerm);
			matches = termMatcher.matches(object);
		}
		return matches;
	}
	
	/**
	 * Serializes an object into a map where the resulting map will have as keys 
	 * the object member names (those which have getter methods) and the value of
	 * each member will match with the value for each key in map.
	 * 
	 * @param object The object to serialize into the map.
	 * 
	 * @return The map resulting from the serialization process
	 * 
	 * @throws MatchingException If there is a problem during map serialization.
	 * 
	 */
	private Map<String, Object> buildMapFromObjectMembers(Object object) throws MatchingException{
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = PropertyUtils.describe(object);
			map.remove("class");
			return map;
		} catch (Exception e) {
			throw new MatchingException(e);
		}
	}
}
