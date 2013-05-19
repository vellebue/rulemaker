package org.rulemaker.parser;

import static org.junit.Assert.*;
import org.junit.Test;

import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;


public class RuleParserLexerTest extends BaseLexerTest {
	
	@Test
	public void shouldRecognizeAValidIntegerNumber() throws Exception {
		Integer number = new Integer("12321");
		TokenStream tokenStream = buildTokenStream(number.toString());
		Token token = tokenStream.LT(1);
		assertEquals(number.toString(), token.getText());
	}
	
	@Test
	public void shouldRecognizeAValidFolatingPointNumber() throws Exception {
		Double number = new Double("2.76");
		TokenStream tokenStream = buildTokenStream(number.toString());
		Token token = tokenStream.LT(1);
		assertEquals(number.toString(), token.getText());
	}

}
