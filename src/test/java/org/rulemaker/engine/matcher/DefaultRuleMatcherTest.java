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
		john.setHeight(1.72f);
		context.addFact(john);
		Person jaimie = new Person("Jaimie");
		jaimie.setAge(25);
		jaimie.setHeight(1.96f);
		context.addFact(jaimie);
		Person tommy = new Person("Tommy");
		tommy.setAge(29);
		tommy.setHeight(1.78f);
		context.addFact(tommy);
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
	
	@Test
	public void shouldMatchARuleWithTwoIndependentConditionsAndTwoFactsSatisfyingThem() throws Exception {
		Rule rule = new Rule(Arrays.asList(
				new Condition(Arrays.asList(new Term("name", Term.TermType.STRING, "John"))),
				new Condition(Arrays.asList(new Term("name", Term.TermType.STRING, "Jaimie")))), 
				new ArrayList<Action>());
		RuleMatcher matcher = new DefaultRuleMatcher();
		List<Object> matcherResults = matcher.matches(context, rule);
		assertEquals(2, matcherResults.size());
		assertEquals("John", ((Person) matcherResults.get(0)).getName());
		assertEquals("Jaimie", ((Person) matcherResults.get(1)).getName());
	}
	
	@Test
	public void shouldMatchARuleWithTwoDependingConditionsByIdentifierAndTwoFactsSatisfyingThem() throws Exception {
		Rule rule = new Rule(Arrays.asList(
				new Condition(Arrays.asList(new Term("name", Term.TermType.STRING, "John"),
						                    new Term("age", Term.TermType.IDENTIFIER, "X"))),
				new Condition(Arrays.asList(new Term(DefaultRuleMatcher.CONDITION_CLASS_TYPE, Term.TermType.STRING, 
						                                  "org.rulemaker.engine.matcher.Person", true),
						                    new Term(DefaultRuleMatcher.FACT_CONSTRAINT, Term.TermType.EXPRESSION, "X < age", true)))), 
				new ArrayList<Action>());
		RuleMatcher matcher = new DefaultRuleMatcher();
		List<Object> matcherResults = matcher.matches(context, rule);
		assertEquals(2, matcherResults.size());
		assertEquals("John", ((Person) matcherResults.get(0)).getName());
		assertEquals("Jaimie", ((Person) matcherResults.get(1)).getName());
	}
	
	@Test
	public void shouldMatchARuleWithTwoDependingConditionsByReferringPreviousMatchingAndTwoFactsSatisfyingThem() throws Exception {
		Rule rule = new Rule(Arrays.asList(
				new Condition(Arrays.asList(new Term(DefaultRuleMatcher.FACT_CONSTRAINT, Term.TermType.EXPRESSION, "age > 20", true))),
				new Condition(Arrays.asList(new Term(DefaultRuleMatcher.FACT_CONSTRAINT, Term.TermType.EXPRESSION, "(age > 20) && (age < _1.age)", true)))), 
				new ArrayList<Action>());
		RuleMatcher matcher = new DefaultRuleMatcher();
		List<Object> matcherResults = matcher.matches(context, rule);
		assertEquals(2, matcherResults.size());
		assertEquals("Tommy", ((Person) matcherResults.get(0)).getName());
		assertEquals("Jaimie", ((Person) matcherResults.get(1)).getName());
	}
	
	@Test
	public void shouldMatchARuleWithTreeConditionsAndThreeMatchingFacts() throws Exception {
		Rule rule = new Rule(Arrays.asList(
				new Condition(Arrays.asList(new Term("age", Term.TermType.IDENTIFIER, "X"))),
				new Condition(Arrays.asList(new Term(DefaultRuleMatcher.FACT_CONSTRAINT, Term.TermType.EXPRESSION, "age < X", true))),
				new Condition(Arrays.asList(new Term(DefaultRuleMatcher.FACT_CONSTRAINT, Term.TermType.EXPRESSION, "age < _2.age", true)))), 
				new ArrayList<Action>());
		RuleMatcher matcher = new DefaultRuleMatcher();
		List<Object> matcherResults = matcher.matches(context, rule);
		assertEquals(3, matcherResults.size());
		assertEquals("Tommy", ((Person) matcherResults.get(0)).getName());
		assertEquals("Jaimie", ((Person) matcherResults.get(1)).getName());
		assertEquals("John", ((Person) matcherResults.get(2)).getName());
	}

}
