package org.rulemaker.engine.matcher;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.rulemaker.engine.matcher.TermMatcher.Factory;
import org.rulemaker.model.Term;

import static org.junit.Assert.*;

public class IdentifierTermMatcherTest {
	
	@Test
	public void shouldBuildAnIdentifierTestMatcherForIdentifierTerm() throws Exception {
		Term term = new Term("name", Term.TermType.IDENTIFIER, "X");
		TermMatcher matcher = Factory.buildTermMatcher(new HashMap<String, Object>(), term);
		assertTrue(matcher instanceof IdentifierTermMatcher);
	}
	
	@Test
	public void shouldMatchAnIdentifierRegisteredBeforeWithTheSameValue() throws Exception {
		Term term = new Term("name", Term.TermType.IDENTIFIER, "X");
		Map<String, Object> variablesMap = new HashMap<String, Object>();
		variablesMap.put("X", "John");
		TermMatcher matcher = Factory.buildTermMatcher(variablesMap, term);
		Person targetPerson = new Person("John");
		assertTrue(matcher.matches(targetPerson));
	}
	
	@Test
	public void shouldNotMatchAnIdentifierRegisteredBeforeWithDifferentValue() throws Exception {
		Term term = new Term("name", Term.TermType.IDENTIFIER, "X");
		Map<String, Object> variablesMap = new HashMap<String, Object>();
		variablesMap.put("X", "Jaime");
		TermMatcher matcher = Factory.buildTermMatcher(variablesMap, term);
		Person targetPerson = new Person("John");
		assertFalse(matcher.matches(targetPerson));
	}
	
	@Test
	public void shouldMatchAnIdentifierNotRegisteredBeforeAndRegisterItWhithCurrentValue() throws Exception {
		Term term = new Term("name", Term.TermType.IDENTIFIER, "X");
		Map<String, Object> variablesMap = new HashMap<String, Object>();
		TermMatcher matcher = Factory.buildTermMatcher(variablesMap, term);
		Person targetPerson = new Person("John");
		assertTrue(matcher.matches(targetPerson));
		assertTrue(variablesMap.containsKey("X"));
		assertEquals(variablesMap.get("X"), "John");
	}
	
	@Test
	public void shouldNotMatchAnObjectWithNoMatchingPropertyName() throws Exception {
		Term term = new Term("surname", Term.TermType.IDENTIFIER, "X");
		Map<String, Object> variablesMap = new HashMap<String, Object>();
		TermMatcher matcher = Factory.buildTermMatcher(variablesMap, term);
		Person targetPerson = new Person("John");
		assertFalse(matcher.matches(targetPerson));
	}

}
