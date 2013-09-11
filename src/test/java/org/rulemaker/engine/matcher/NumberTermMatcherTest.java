package org.rulemaker.engine.matcher;

import org.junit.Test;
import static org.junit.Assert.*;

import org.rulemaker.engine.EngineContext;
import org.rulemaker.model.Term;

public class NumberTermMatcherTest {
	
	@Test
	public void shouldBuildANumberTermMatcherTestForNumberTermTypes() throws Exception {
		Term term = new Term("age", Term.TermType.NUMBER, "18");
		TermMatcher matcher = TermMatcher.Factory.buildTermMatcher(new EngineContext(null), 
				term);
		assertTrue(matcher instanceof NumberTermMatcher);
	}
	
	@Test
	public void shouldMatchNumberTermWithMatchingIntegerValue() throws Exception {
		Term term = new Term("age", Term.TermType.NUMBER, "18");
		TermMatcher matcher = TermMatcher.Factory.buildTermMatcher(new EngineContext(null), 
				term);
		Person targetPerson = new Person("John");
		targetPerson.setAge(18);
		assertTrue(matcher.matches(targetPerson));
	}
	
	@Test
	public void shouldMatchNumberTermWithMatchingDoubleValue() throws Exception {
		Term term = new Term("salary", Term.TermType.NUMBER, "1576.21");
		TermMatcher matcher = TermMatcher.Factory.buildTermMatcher(new EngineContext(null), 
				term);
		Person targetPerson = new Person("John");
		targetPerson.setSalary(1576.21);
		assertTrue(matcher.matches(targetPerson));
	}
	
	@Test
	public void shouldMatchNumberTermWithMatchingFloatValue() throws Exception {
		Term term = new Term("height", Term.TermType.NUMBER, "1.76");
		TermMatcher matcher = TermMatcher.Factory.buildTermMatcher(new EngineContext(null), 
				term);
		Person targetPerson = new Person("John");
		targetPerson.setHeight(1.76f);
		assertTrue(matcher.matches(targetPerson));
	}
	
	@Test
	public void shouldNotMatchNumberTermWithNoMatchingNumberValue() throws Exception {
		Term term = new Term("height", Term.TermType.NUMBER, "1.76");
		TermMatcher matcher = TermMatcher.Factory.buildTermMatcher(new EngineContext(null), 
				term);
		Person targetPerson = new Person("John");
		targetPerson.setHeight(1.92f);
		assertFalse(matcher.matches(targetPerson));
	}
	
	@Test
	public void shouldNotMatchNonExistingNumberTerm() throws Exception {
		Term term = new Term("strenght", Term.TermType.NUMBER, "0.34");
		TermMatcher matcher = TermMatcher.Factory.buildTermMatcher(new EngineContext(null), 
				term);
		Person targetPerson = new Person("John");
		assertFalse(matcher.matches(targetPerson));
	}
}
