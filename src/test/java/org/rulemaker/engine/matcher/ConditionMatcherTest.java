package org.rulemaker.engine.matcher;

import java.util.Arrays;

import org.junit.Test;
import static org.junit.Assert.*;

import org.rulemaker.engine.EngineContext;
import org.rulemaker.model.Condition;
import org.rulemaker.model.Term;

public class ConditionMatcherTest {
	
	@Test
	public void shouldMatchBasicStringCondition() throws Exception {
		Condition condition = new Condition(Arrays.asList(new Term("name", Term.TermType.STRING, "John")));
		Person person = new Person("John");
		EngineContext context = new EngineContext(null);
		ConditionMatcher matcher = new ConditionMatcher();
		matcher.setEngineContext(context);
		assertTrue(matcher.matches(person, condition));
	}
	
	@Test
	public void shouldMatchIdentifierFirstTimeAndRegisterItInGlobalVariablesMap() throws Exception {
		Condition condition = new Condition(Arrays.asList(new Term("name", Term.TermType.IDENTIFIER, "X")));
		Person person = new Person("John");
		EngineContext context = new EngineContext(null);
		ConditionMatcher matcher = new ConditionMatcher();
		matcher.setEngineContext(context);
		assertTrue(matcher.matches(person, condition));
		Object value = context.getGobalVariablesMap().get("X");
		assertNotNull(value);
		assertTrue(value.equals("John"));
	}
	
	@Test
	public void shouldMatchConstraintConditionReferringCurrentObjectMembers() throws Exception {
		Condition condition = new Condition(Arrays.asList(
				new Term(DefaultRuleMatcher.FACT_CONSTRAINT, 
						   Term.TermType.EXPRESSION, "name.equals('John') && age.equals(18)", true)));
		Person person = new Person("John");
		person.setAge(18);
		EngineContext context = new EngineContext(null);
		ConditionMatcher matcher = new ConditionMatcher();
		matcher.setEngineContext(context);
		assertTrue(matcher.matches(person, condition));
		// Local members 'name' and 'age' must not be present in global variables map
		assertNull(context.getGobalVariablesMap().get("name"));
		assertNull(context.getGobalVariablesMap().get("age"));
	}
	
	@Test
	public void shouldMatchConstraintConditionReferringRegisteredClassType() throws Exception {
		Condition condition = new Condition(Arrays.asList(
				new Term(DefaultRuleMatcher.CONDITION_CLASS_TYPE, 
						   Term.TermType.STRING, "person", true)));
		Person person = new Person("John");
		EngineContext context = new EngineContext(null);
		context.registerClass("person", Person.class);
		ConditionMatcher matcher = new ConditionMatcher();
		matcher.setEngineContext(context);
		assertTrue(matcher.matches(person, condition));
	}
	
	@Test
	public void shouldMatchMultipleTermCondition() throws Exception {
		Condition condition = new Condition(Arrays.asList(
				new Term("name", Term.TermType.STRING, "John"),
				new Term("age", Term.TermType.NUMBER, "18")));
		Person person = new Person("John");
		person.setAge(18);
		EngineContext context = new EngineContext(null);
		ConditionMatcher matcher = new ConditionMatcher();
		matcher.setEngineContext(context);
		assertTrue(matcher.matches(person, condition));
	}
	
	@Test
	public void shouldNotMatchMultipleTermConditionWhenOneTermDoesNotMatch() throws Exception {
		Condition condition = new Condition(Arrays.asList(
				new Term("name", Term.TermType.STRING, "John"),
				new Term("age", Term.TermType.NUMBER, "18")));
		Person person = new Person("John");
		person.setAge(19);
		EngineContext context = new EngineContext(null);
		ConditionMatcher matcher = new ConditionMatcher();
		matcher.setEngineContext(context);
		assertFalse(matcher.matches(person, condition));
	}
}
