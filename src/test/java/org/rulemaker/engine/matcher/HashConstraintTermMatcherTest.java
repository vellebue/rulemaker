package org.rulemaker.engine.matcher;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Test;
import org.rulemaker.model.Term;

import static org.junit.Assert.*;

public class HashConstraintTermMatcherTest {
	
	@Test
	public void shouldBuildAConstraintTermMatcherForAConstraintTerm() throws Exception {
		Term term = new Term(DefaultRuleMatcher.FACT_CONSTRAINT, Term.TermType.EXPRESSION, "X > 25", true);
		TermMatcher matcher = TermMatcher.Factory.buildTermMatcher(new HashMap<String, Object>(), term);
		assertTrue(matcher instanceof HashConstraintTermMatcher);
	}
	
	@Test
	public void shouldMatchAnObjectWhichSatisfiesConstraint() throws Exception {
		Term term = new Term(DefaultRuleMatcher.FACT_CONSTRAINT, Term.TermType.EXPRESSION, "age > 25", true);
		Map<String, Object> expressionMap = new HashMap<String, Object>();
		Person targetPerson = new Person("John");
		targetPerson.setAge(26);
		transferObjectFieldsToMap(expressionMap, targetPerson);
		TermMatcher matcher = TermMatcher.Factory.buildTermMatcher(expressionMap, term);
		assertTrue(matcher.matches(targetPerson));
	}

	@Test
	public void shouldNotMatchAnObjectWhichNotSatisfiesConstraint() throws Exception {
		Term term = new Term(DefaultRuleMatcher.FACT_CONSTRAINT, Term.TermType.EXPRESSION, "age > 25", true);
		Map<String, Object> expressionMap = new HashMap<String, Object>();
		Person targetPerson = new Person("John");
		targetPerson.setAge(24);
		transferObjectFieldsToMap(expressionMap, targetPerson);
		TermMatcher matcher = TermMatcher.Factory.buildTermMatcher(expressionMap, term);
		assertFalse(matcher.matches(targetPerson));
	}
	
	@Test(expected = Exception.class)
	public void shouldNotMatchAnObjectWithConstraintNotReferringRegisteredFields() throws Exception {
		Term term = new Term(DefaultRuleMatcher.FACT_CONSTRAINT, Term.TermType.EXPRESSION, "thickness > 12.3", true);
		Map<String, Object> expressionMap = new HashMap<String, Object>();
		Person targetPerson = new Person("John");
		transferObjectFieldsToMap(expressionMap, targetPerson);
		TermMatcher matcher = TermMatcher.Factory.buildTermMatcher(expressionMap, term);
		@SuppressWarnings("unused")
		boolean notObtainedResultDueToExpectedException = matcher.matches(targetPerson);
	}
	
	@Test
	public void shouldNotMatchAnObjectWithAConstraintThatIsNotAnExpression() throws Exception {
		Term term = new Term(DefaultRuleMatcher.FACT_CONSTRAINT, Term.TermType.NUMBER, "12.3", true);
		Map<String, Object> expressionMap = new HashMap<String, Object>();
		Person targetPerson = new Person("John");
		transferObjectFieldsToMap(expressionMap, targetPerson);
		TermMatcher matcher = TermMatcher.Factory.buildTermMatcher(expressionMap, term);
		assertFalse(matcher.matches(targetPerson));
	}
	
	private void transferObjectFieldsToMap(Map<String, Object> variablesMap, Object object) throws Exception {
		@SuppressWarnings("unchecked")
		Map <String,Object> propertiesMap = PropertyUtils.describe(object);
		for (String aField : propertiesMap.keySet()) {
			// Exclude class field to be added (not regular field)
			if (!aField.equals("class")) {
				variablesMap.put(aField, propertiesMap.get(aField));
			}
		}
	}
}
