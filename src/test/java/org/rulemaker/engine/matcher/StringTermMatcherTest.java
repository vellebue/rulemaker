package org.rulemaker.engine.matcher;

import org.junit.Test;
import static  org.junit.Assert.*;

import org.rulemaker.engine.EngineContext;
import org.rulemaker.model.Term;

public class StringTermMatcherTest {
		
	@Test
	public void shouldBuildAStringTermMatcherForStringTermType() throws Exception {
		Term term = new Term("name", Term.TermType.STRING, "John");
		TermMatcher matcher = TermMatcher.Factory.buildTermMatcher(
				new EngineContext(null), term);
		assertTrue(matcher instanceof StringTermMatcher);
	}
	
	@Test
	public void shouldMatchAMatchingObjectWithMatchingTermNameAndValue() {
		Person matchingObject = new Person("John");
		Term term = new Term("name", Term.TermType.STRING, "John");
		TermMatcher matcher = TermMatcher.Factory.buildTermMatcher(
				new EngineContext(null), term);
		assertTrue(matcher.matches(matchingObject));
	}
	
	@Test
	public void shouldNotMatchAMatchingObjectWithDifferentTermValue() {
		Person matchingObject = new Person("Jaime");
		Term term = new Term("name", Term.TermType.STRING, "John");
		TermMatcher matcher = TermMatcher.Factory.buildTermMatcher(
				new EngineContext(null), term);
		assertFalse(matcher.matches(matchingObject));
	}
	
	@Test
	public void shouldNotMatchAMatchingObjectWithNoMemberNamedAsShownInTerm() {
		Person matchingObject = new Person("Jaime");
		Term term = new Term("surname", Term.TermType.STRING, "John");
		TermMatcher matcher = TermMatcher.Factory.buildTermMatcher(
				new EngineContext(null), term);
		assertFalse(matcher.matches(matchingObject));
	}

}
