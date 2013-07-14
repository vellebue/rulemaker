package org.rulemaker.parser;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.rulemaker.model.Action;
import org.rulemaker.model.Condition;
import org.rulemaker.model.Rule;
import org.rulemaker.model.Term;

public class RuleListParserTest extends BaseParserTest {

	@Test
	public void shoulParseASingleElementRuleList() throws Exception {
		RulesParserParser parser = buildParser("(val = X)(name='Joe') -> update(level=11)");
		Rule expectedRule = new Rule(
				Arrays.asList(
						new Condition(Arrays.asList(new Term("val", Term.TermType.IDENTIFIER, "X"))),
						new Condition(Arrays.asList(new Term("name", Term.TermType.STRING, "Joe")))), 
			    Arrays.asList(
			    		new Action("update", Arrays.asList(new Term("level", Term.TermType.NUMBER, "11")))));
		parser.ruleSet();
		List<Rule> ruleList = parser.getRuleList();
		assertEquals("Expected one rule parsed", 1, ruleList.size());
		assertEquals(expectedRule, ruleList.get(0));
	}
	
	@Test
	public void shouldParseTwoRulesList() throws Exception {
		RulesParserParser parser = buildParser("(val = X)(name='Joe') -> update(level=11) ;" +
											   "(origin=1) -> insert(message='enabled')");
		Rule expectedRule1 = new Rule(
				Arrays.asList(
						new Condition(Arrays.asList(new Term("val", Term.TermType.IDENTIFIER, "X"))),
						new Condition(Arrays.asList(new Term("name", Term.TermType.STRING, "Joe")))), 
			    Arrays.asList(
			    		new Action("update", Arrays.asList(new Term("level", Term.TermType.NUMBER, "11")))));
		Rule expectedRule2 = new Rule(
				Arrays.asList(
						new Condition(Arrays.asList(new Term("origin", Term.TermType.NUMBER, "1")))),
				Arrays.asList(new Action("insert", Arrays.asList(new Term("message", Term.TermType.STRING, "enabled")))));
		parser.ruleSet();
		List<Rule> ruleList = parser.getRuleList();
		assertEquals("Expected two rules parsed", 2, ruleList.size());
		assertEquals(Arrays.asList(expectedRule1, expectedRule2), ruleList);
	}
}
