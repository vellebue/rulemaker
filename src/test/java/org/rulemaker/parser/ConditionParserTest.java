package org.rulemaker.parser;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.rulemaker.model.Condition;
import org.rulemaker.model.Term;

public class ConditionParserTest extends BaseParserTest {
	
	@Test
	public void shouldRecognizeAnEmptyCondition() throws Exception{
		RulesParserParser parser = this.buildParser("()");
		parser.condition();
		List<Term> termsList = parser.getCurrentTokenTermList();
		assertEquals("No terms expected", 0, termsList.size());
	}
	
	@Test
	public void shouldRecognizeAValidSingleTermCondition() throws Exception {
		RulesParserParser parser = this.buildParser("(documentType=11)");
		parser.condition();
		List<Term> termsList = parser.getCurrentTokenTermList();
		assertEquals("One term expected", 1, termsList.size());
		Term obtainedTerm = termsList.get(0);
		Term expectedTerm = new Term("documentType", Term.TermType.NUMBER, "11");
		assertEquals(expectedTerm, obtainedTerm);
	}
	
	@Test
	public void shouldRecognizeMultipleTermsConditions() throws Exception {
		Term [] expectedTermsArray = {new Term("documentType", Term.TermType.NUMBER, "11"), 
				                      new Term("targetType", Term.TermType.IDENTIFIER, "X"),
				                      new Term("value", Term.TermType.STRING, "John Doe")};
		RulesParserParser parser = this.buildParser("(documentType=11, targetType=X, value='John Doe')");
		parser.condition();
		List<Term> termsList = parser.getCurrentTokenTermList();
		List<Term> expectedTermList = new ArrayList<Term>();
		expectedTermList.addAll(Arrays.asList(expectedTermsArray));
		assertEquals(expectedTermList, termsList);
	}
		
	@Test
	public void shouldRecognizeMultipleConditions() throws Exception {
		Condition []expectedContitionsArray = {
				new Condition(Arrays.asList(new Term []{new Term("documentType", Term.TermType.NUMBER, "11")})),
				new Condition(Arrays.asList(new Term []{new Term("targetType", Term.TermType.IDENTIFIER, "X"),
				                                        new Term("value", Term.TermType.STRING, "John Doe")}))
		};
		RulesParserParser parser = this.buildParser("(documentType=11) (targetType=X, value='John Doe')");
		parser.conditionList();
		List<Condition> conditionList = parser.getConditionList();
		List<Condition> expectedConditionList = Arrays.asList(expectedContitionsArray);
		assertEquals(expectedConditionList, conditionList);
	}
	
	@Test
	public void shouldRecognizeSharpTermsInMultipleConditions() throws Exception {
		RulesParserParser parser = this.buildParser("(#type='bill')  (documentType=11) (targetType=X, value='John Doe')");
		Condition []expectedContitionsArray = {
				new Condition(Arrays.asList(new Term []{new Term("type", Term.TermType.STRING, "bill", true)})),
				new Condition(Arrays.asList(new Term []{new Term("documentType", Term.TermType.NUMBER, "11")})),
				new Condition(Arrays.asList(new Term []{new Term("targetType", Term.TermType.IDENTIFIER, "X"),
				                                        new Term("value", Term.TermType.STRING, "John Doe")}))
		};
		parser.conditionList();
		List<Condition> conditionList = parser.getConditionList();
		List<Condition> expectedConditionList = Arrays.asList(expectedContitionsArray);
		assertEquals(expectedConditionList, conditionList);
	}
	
	@Test
	public void shouldRecognizeASimpleExpressionCondition() throws Exception {
		RulesParserParser parser = this.buildParser("(value=${16 + 148 - a})");
		Condition []expectedContitionsArray = {
				new Condition(Arrays.asList(new Term []{new Term("value", Term.TermType.EXPRESSION, "16 + 148 - a")}))};
		parser.conditionList();
		List<Condition> conditionList = parser.getConditionList();
		List<Condition> expectedConditionList = Arrays.asList(expectedContitionsArray);
		assertEquals(expectedConditionList, conditionList);
	}
	
	@Test
	public void shouldRecognizeAComplexExpressionCondition() throws Exception {
		RulesParserParser parser = this.buildParser("(value=${new String[]{\"John\", \"Doe\"\\}[1]})");
		Condition []expectedContitionsArray = {
				new Condition(Arrays.asList(new Term []{new Term("value", Term.TermType.EXPRESSION, "new String[]{\"John\", \"Doe\"}[1]")}))};
		parser.conditionList();
		List<Condition> conditionList = parser.getConditionList();
		List<Condition> expectedConditionList = Arrays.asList(expectedContitionsArray);
		assertEquals(expectedConditionList, conditionList);
	}
}
