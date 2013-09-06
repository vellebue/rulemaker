package org.rulemaker.engine.matcher;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import static org.junit.Assert.*;

import org.rulemaker.model.Term;

public class HashTypeTermMatcherTest {
	
	@Test
	public void shouldBuildATypeTermMatcherWhenGivenATypeTerm() throws Exception {
		Term term = new Term(DefaultRuleMatcher.CONDITION_CLASS_TYPE, Term.TermType.STRING, 
				"org.rulemaker.engine.matcher.Person", true);
		TermMatcher matcher = TermMatcher.Factory.buildTermMatcher(new HashMap<String, Object>(), term);
		assertTrue(matcher instanceof HashTypeTermMatcher);
	}
	
	@Test
	public void shouldMatchAnObjectGivenItsClassName() throws Exception {
		Term term = new Term(DefaultRuleMatcher.CONDITION_CLASS_TYPE, Term.TermType.STRING, 
				"org.rulemaker.engine.matcher.Person", true);
		TermMatcher matcher = TermMatcher.Factory.buildTermMatcher(new HashMap<String, Object>(), term);
		((HashTypeTermMatcher) matcher).setClassSinonymsMap(new HashMap<String, Class<?>>());
		Person person = new Person("John");
		assertTrue(matcher.matches(person));
	}
	
	@Test
	public void shouldMatchAnObjectGivenItsClassSinonym() throws Exception {
		Term term = new Term(DefaultRuleMatcher.CONDITION_CLASS_TYPE, Term.TermType.STRING, 
				"person", true);
		TermMatcher matcher = TermMatcher.Factory.buildTermMatcher(new HashMap<String, Object>(), term);
		Map<String, Class<?>> sinonymsMap = new HashMap<String, Class<?>>();
		sinonymsMap.put("person", Person.class);
		((HashTypeTermMatcher) matcher).setClassSinonymsMap(sinonymsMap);
		Person person = new Person("John");
		assertTrue(matcher.matches(person));
	}
	
	@Test
	public void shouldNotMatchAnObjectWithNoCompatibleClassType() throws Exception {
		Term term = new Term(DefaultRuleMatcher.CONDITION_CLASS_TYPE, Term.TermType.STRING, 
				"java.lang.String", true);
		TermMatcher matcher = TermMatcher.Factory.buildTermMatcher(new HashMap<String, Object>(), term);
		((HashTypeTermMatcher) matcher).setClassSinonymsMap(new HashMap<String, Class<?>>());
		Person person = new Person("John");
		assertFalse(matcher.matches(person));
	}
	
	@Test
	public void shouldNotMatchAnObjectWithNonExistingClassType() throws Exception {
		Term term = new Term(DefaultRuleMatcher.CONDITION_CLASS_TYPE, Term.TermType.STRING, 
				"com.fake.NonExistingClass", true);
		TermMatcher matcher = TermMatcher.Factory.buildTermMatcher(new HashMap<String, Object>(), term);
		((HashTypeTermMatcher) matcher).setClassSinonymsMap(new HashMap<String, Class<?>>());
		Person person = new Person("John");
		assertFalse(matcher.matches(person));
	}

}
