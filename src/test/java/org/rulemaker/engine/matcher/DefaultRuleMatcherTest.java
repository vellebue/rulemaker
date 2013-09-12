package org.rulemaker.engine.matcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.rulemaker.engine.EngineContext;
import org.rulemaker.model.Action;
import org.rulemaker.model.Condition;
import org.rulemaker.model.Rule;
import org.rulemaker.model.Term;

import static org.junit.Assert.*;

public class DefaultRuleMatcherTest {
	
	private EngineContext context = new EngineContext(null);
	
	@Before
	public void setupEngineContext() {
		Person john = new Person("John");
		john.setAge(18);
		context.addFact(john);
	}
	
	@Test
	public void shouldMatchARuleWithOneConditionAndOneFactSatisfiyingRule() 
			throws Exception {
		Rule rule = new Rule(Arrays.asList(
				new Condition(Arrays.asList(new Term("name", Term.TermType.STRING, "John")))), 
				new ArrayList<Action>());
		RuleMatcher matcher = new DefaultRuleMatcher();
		List<Object> matcherResults = matcher.matches(context, rule);
		assertEquals(1, matcherResults.size());
		assertEquals("John", ((Person) matcherResults.get(0)).getName());
	}

}
