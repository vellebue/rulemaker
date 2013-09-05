package org.rulemaker.engine.matcher;

import java.util.List;

import org.rulemaker.engine.EngineContext;
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
		// TODO Auto-generated method stub
		return null;
	}

}
