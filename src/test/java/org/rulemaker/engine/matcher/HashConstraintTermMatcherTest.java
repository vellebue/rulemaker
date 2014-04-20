package org.rulemaker.engine.matcher;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Test;
import org.rulemaker.engine.EngineContext;
import org.rulemaker.engine.matcher.exception.MatchingException;
import org.rulemaker.model.Term;

import static org.junit.Assert.*;

public class HashConstraintTermMatcherTest {
	
	@Test
	public void shouldBuildAConstraintTermMatcherForAConstraintTerm() throws Exception {
		Term term = new Term(DefaultRuleMatcher.FACT_CONSTRAINT, Term.TermType.EXPRESSION, "X > 25", true);
		TermMatcher matcher = TermMatcher.Factory.buildTermMatcher(
				new EngineContext(null), new HashMap<String, Object>(), term);
		assertTrue(matcher instanceof HashConstraintTermMatcher);
	}
	
	@Test
	public void shouldMatchAnObjectWhichSatisfiesConstraint() throws Exception {
		Term term = new Term(DefaultRuleMatcher.FACT_CONSTRAINT, Term.TermType.EXPRESSION, "age > 25", true);
		EngineContext context = new EngineContext(null); 
		Map<String, Object> expressionMap = context.getGobalVariablesMap();
		Person targetPerson = new Person("John");
		targetPerson.setAge(26);
		transferObjectFieldsToMap(expressionMap, targetPerson);
		TermMatcher matcher = TermMatcher.Factory.buildTermMatcher(context, expressionMap, term);
		assertTrue(matcher.matches(targetPerson));
	}
	
	@Test
	public void shouldMatchAnObjectWhichSatisfiesConstraintAsNotNullObjectValue() throws Exception {
		Term term = new Term(DefaultRuleMatcher.FACT_CONSTRAINT, Term.TermType.EXPRESSION, "age", true);
		EngineContext context = new EngineContext(null); 
		Map<String, Object> expressionMap = context.getGobalVariablesMap();
		Person targetPerson = new Person("John");
		targetPerson.setAge(26);
		transferObjectFieldsToMap(expressionMap, targetPerson);
		TermMatcher matcher = TermMatcher.Factory.buildTermMatcher(context, expressionMap, term);
		assertTrue(matcher.matches(targetPerson));
	}
	
	@Test
	public void shouldNotMatchAnObjectWhichNotSatisfiesConstraintAsNullObjectValue() throws Exception {
		Term term = new Term(DefaultRuleMatcher.FACT_CONSTRAINT, Term.TermType.EXPRESSION, "salary", true);
		EngineContext context = new EngineContext(null); 
		Map<String, Object> expressionMap = context.getGobalVariablesMap();
		Person targetPerson = new Person("John");
		targetPerson.setAge(26);
		transferObjectFieldsToMap(expressionMap, targetPerson);
		TermMatcher matcher = TermMatcher.Factory.buildTermMatcher(context, expressionMap, term);
		assertFalse(matcher.matches(targetPerson));
	}


	@Test
	public void shouldNotMatchAnObjectWhichNotSatisfiesConstraint() throws Exception {
		Term term = new Term(DefaultRuleMatcher.FACT_CONSTRAINT, Term.TermType.EXPRESSION, "age > 25", true);
		EngineContext context = new EngineContext(null); 
		Map<String, Object> expressionMap = context.getGobalVariablesMap();
		Person targetPerson = new Person("John");
		targetPerson.setAge(24);
		transferObjectFieldsToMap(expressionMap, targetPerson);
		TermMatcher matcher = TermMatcher.Factory.buildTermMatcher(context, expressionMap, term);
		assertFalse(matcher.matches(targetPerson));
	}
	
	@Test(expected = Exception.class)
	public void shouldNotMatchAnObjectWithConstraintNotReferringRegisteredFields() throws Exception {
		Term term = new Term(DefaultRuleMatcher.FACT_CONSTRAINT, Term.TermType.EXPRESSION, "thickness > 12.3", true);
		EngineContext context = new EngineContext(null); 
		Map<String, Object> expressionMap = context.getGobalVariablesMap();
		Person targetPerson = new Person("John");
		transferObjectFieldsToMap(expressionMap, targetPerson);
		TermMatcher matcher = TermMatcher.Factory.buildTermMatcher(context, expressionMap, term);
		@SuppressWarnings("unused")
		boolean notObtainedResultDueToExpectedException = matcher.matches(targetPerson);
	}
	
	@Test
	public void shouldNotMatchAnObjectWithAConstraintThatIsNotAnExpression() throws Exception {
		Term term = new Term(DefaultRuleMatcher.FACT_CONSTRAINT, Term.TermType.NUMBER, "12.3", true);
		EngineContext context = new EngineContext(null); 
		Map<String, Object> expressionMap = context.getGobalVariablesMap();
		Person targetPerson = new Person("John");
		transferObjectFieldsToMap(expressionMap, targetPerson);
		TermMatcher matcher = TermMatcher.Factory.buildTermMatcher(context, expressionMap, term);
		assertFalse(matcher.matches(targetPerson));
	}
	
	@Test(expected = MatchingException.class)
	public void shouldThrowAMatchingExceptionWhenThereIsAnIssueWithConstraintExpression() throws Exception {
		// Notice the expression is "aged > 25" but variable in Person object is named age. There will be
		// an exception evaluating the expression.
		Term term = new Term(DefaultRuleMatcher.FACT_CONSTRAINT, Term.TermType.EXPRESSION, "aged > 25", true);
		EngineContext context = new EngineContext(null); 
		Map<String, Object> expressionMap = context.getGobalVariablesMap();
		Person targetPerson = new Person("John");
		targetPerson.setAge(24);
		transferObjectFieldsToMap(expressionMap, targetPerson);
		TermMatcher matcher = TermMatcher.Factory.buildTermMatcher(context, expressionMap, term);
		matcher.matches(targetPerson);
	}
	
	private void transferObjectFieldsToMap(Map<String, Object> variablesMap, Object object) throws Exception {
		@SuppressWarnings("unchecked")
		Map <String,Object> propertiesMap = PropertyUtils.describe(object);
		propertiesMap.remove("class");
		variablesMap.putAll(propertiesMap);
	}
}
