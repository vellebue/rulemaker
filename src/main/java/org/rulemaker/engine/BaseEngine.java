package org.rulemaker.engine;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.rulemaker.engine.actionengine.ActionEngine;
import org.rulemaker.engine.actionengine.BaseActionEngine;
import org.rulemaker.engine.executors.actions.CreateActionExecutor;
import org.rulemaker.engine.executors.actions.DeleteActionExecutor;
import org.rulemaker.engine.executors.actions.UpdateActionExecutor;
import org.rulemaker.engine.matcher.DefaultRuleMatcher;
import org.rulemaker.engine.matcher.RuleMatcher;
import org.rulemaker.model.Action;
import org.rulemaker.model.Rule;
import org.rulemaker.parser.ParserException;
import org.rulemaker.parser.RulesParser;

/**
 * Base class for all engine types. An engine
 * is build providing its rules (as a rule list or
 * as a text describing them). Later you will
 * provide fact objects directly or classified 
 * by domains. 
 * 
 * Optionally you can define a k factor that 
 * ensures the rule engine will not execute more 
 * rule steps than k*n, being n the number of facts
 * provided. 
 * 
 * @author &Aacute;ngel Garc&iacute;a Bastanchuri
 *
 */
public abstract class BaseEngine implements Engine {
	
	private EngineContext context;
	private Integer kFactor;
	private ActionEngine actionEngine;
	private RuleMatcher ruleMatcher;
	private int currentStep = 0;
	
	/**
	 * Builds an engine given its rules
	 * @param rules The rules this engine will use.
	 * 
	 */
	public BaseEngine(List<Rule> rules) {
		context = new EngineContext(rules);
		ruleMatcher = new DefaultRuleMatcher();
		actionEngine = new BaseActionEngine();
		actionEngine.setEngineContext(context);
		actionEngine.addExecutorClass("create", CreateActionExecutor.class);
		actionEngine.addExecutorClass("update", UpdateActionExecutor.class);
		actionEngine.addExecutorClass("delete", DeleteActionExecutor.class);
	}
	
	/**
	 * Builds an engine when the rules are given 
	 * as a text describing them.
	 * 
	 * @param rulesText The text containing the description of the rules.
	 * 
	 * @throws EngineException If there is a problem while parsing the rules.
	 * 
	 */
	public BaseEngine(String rulesText) throws EngineException {
		this(parseRules(rulesText));
	}
	
	/**
	 * Gets the engine context in this rule engine.
	 * 
	 * @return An EngineContext instance.
	 */
	public final EngineContext getEngineContext() {
		return context;
	}
	
	public final void registerClassSinonym(String className, Class<?> clazz) {
		context.registerClass(className, clazz);
	}

	/**
	 * Gets current k factor.
	 * 
	 * @return the k factor
	 */
	public final Integer getkFactor() {
		return kFactor;
	}

	/**
	 * Set a k Factor value.
	 * 
	 * @param kFactor The k factor value.
	 */
	public final void setkFactor(Integer kFactor) {
		this.kFactor = kFactor;
	}

	/**
	 * Gets the current rule matcher.
	 * 
	 * @return The rule matcher.
	 * 
	 */
	protected final RuleMatcher getRuleMatcher() {
		return ruleMatcher;
	}

	/**
	 * Sets the rule matcher for this engine.
	 * 
	 * @param ruleMatcher The rule matcher to be assigned.
	 */
	protected final void setRuleMatcher(RuleMatcher ruleMatcher) {
		this.ruleMatcher = ruleMatcher;
	}

	private static List<Rule> parseRules(String sourceData) throws EngineException {
        try {
			return RulesParser.getInstance().parseRules(sourceData);
		} catch (ParserException e) {
			throw new EngineException(e);
		}
    }
	
	public final void addFact(Object fact) {
		this.context.addFact(fact);
	}
	
	public final void addFact(String factDomain, Object fact) {
		this.context.addFact(factDomain, fact);
	}

	public boolean runStep() throws EngineException {
		Iterator<Rule> iterator = context.getRulesList().iterator();
		Rule matchingRule = null;
		Map<String, Object> matchingFactsMap = null;		
		while((matchingFactsMap == null) && iterator.hasNext()) {
			matchingRule = iterator.next();
			matchingFactsMap = ruleMatcher.matches(context, matchingRule);
		}
		if (matchingFactsMap != null) {
			executeActionExecutors(matchingFactsMap, matchingRule);
			currentStep++;
			return true;
		} else {
			return false;
		}		
	}
	
	private void executeActionExecutors(Map<String, Object> matchingFactsMap, Rule matchingRule) throws EngineException{
		for (Action ruleAction : matchingRule.getActionList()) {
			actionEngine.executeAction(matchingFactsMap, ruleAction);
		}
	}


	public final void runEngine() throws EngineException {
		int numFacts = countFacts();
		while (runStep() && 
			   ((kFactor == null) || (currentStep <= kFactor.intValue() * numFacts )));
		if ((kFactor != null) && (currentStep > kFactor.intValue() * numFacts)) {
			throw new EngineException("Exceded number of engine steps with k factor equals to " + kFactor);
		}
	}
	
	private int countFacts() {
		Map<String, List<Object>> factBase = context.getFactBase();
		int count = 0;
		for (List<Object> aList : factBase.values()) {
			count += (aList != null) ? aList.size() : 0;
		}
		return count;
	}

}
