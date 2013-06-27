package org.rulemaker.parser;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.rulemaker.model.Condition;
import org.rulemaker.model.Rule;
import org.rulemaker.model.Action;
import org.rulemaker.model.Term;

public class RuleParserTest extends BaseParserTest {
	
	@Test
	public void shouldRecognizeARuleWithNoConditions() throws Exception {
		RulesParserParser parser = buildParser("-> action(name='John', surname='Doe')");
		parser.rule();
		Action expectedAction = new Action("action",
				Arrays.asList(new Term("name", Term.TermType.STRING, "John"),
						      new Term("surname", Term.TermType.STRING, "Doe")));
		Rule expectedRule = new Rule(new  ArrayList<Condition>(), 
				                     Arrays.asList(expectedAction));
		assertEquals(expectedRule, parser.getRule());
	}
	
	@Test
	public void shouldRecognizeARuleWhithManyConditionsAndOneAction() throws Exception {
		RulesParserParser parser = buildParser("(age=18) (eyes=X)->action(name='John', surname='Doe')");
		parser.rule();
		List<Condition> expectedConditionList = Arrays.asList(
				  new Condition(Arrays.asList(new Term("age", Term.TermType.NUMBER, "18"))),
				  new Condition(Arrays.asList(new Term("eyes", Term.TermType.IDENTIFIER, "X")))
				); 
		List<Action> expectedActionList = Arrays.asList(
				  new Action("action",
						Arrays.asList(new Term("name", Term.TermType.STRING, "John"),
								      new Term("surname", Term.TermType.STRING, "Doe")))
				);
		Rule expectedRule = new Rule(expectedConditionList, expectedActionList);
		assertEquals(expectedRule, parser.getRule());
	}
	
	@Test
	public void shouldRecognizeARuleWhithManyConditionsAndManyActions() throws Exception {
		RulesParserParser parser = buildParser("(age=18) (eyes=X) -> action(name='John', surname='Doe') update(system=Z)");
		parser.rule();
		List<Condition> expectedConditionList = Arrays.asList(
				  new Condition(Arrays.asList(new Term("age", Term.TermType.NUMBER, "18"))),
				  new Condition(Arrays.asList(new Term("eyes", Term.TermType.IDENTIFIER, "X")))
				); 
		List<Action> expectedActionList = Arrays.asList(
				  new Action("action",
						Arrays.asList(new Term("name", Term.TermType.STRING, "John"),
								      new Term("surname", Term.TermType.STRING, "Doe"))),
			      new Action("update",
			    		Arrays.asList(new Term("system", Term.TermType.IDENTIFIER, "Z")))
				);
		Rule expectedRule = new Rule(expectedConditionList, expectedActionList);
		assertEquals(expectedRule, parser.getRule());
	}

}
