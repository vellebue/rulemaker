package org.rulemaker.parser;

import static org.junit.Assert.*;

import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;
import org.junit.Test;


public class SymbolLexerTest extends BaseLexerTest {
	
	@Test
	public void shouldEqualsSymbolBeRecognized() throws Exception {
		TokenStream tokenStream = buildTokenStream("=");
		Token token = tokenStream.LT(1);
		assertEquals("Expected =", "=", token.getText());
	}
	
	@Test
	public void shouldLeftParenthesisSymbolBeRecognized() throws Exception {
		TokenStream tokenStream = buildTokenStream("(");
		Token token = tokenStream.LT(1);
		assertEquals("Expected (", "(", token.getText());
	}

	@Test
	public void shouldRightParenthesisSymbolBeRecognized() throws Exception {
		TokenStream tokenStream = buildTokenStream(")");
		Token token = tokenStream.LT(1);
		assertEquals("Expected )", ")", token.getText());
	}
	
	@Test
	public void shouldConditionsActionsSeparatorBeRecognized() throws Exception {
		TokenStream tokenStream = buildTokenStream("->");
		Token token = tokenStream.LT(1);
		assertEquals("->", token.getText());
	}
}
