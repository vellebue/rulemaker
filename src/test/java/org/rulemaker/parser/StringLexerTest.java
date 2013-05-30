package org.rulemaker.parser;

import static org.junit.Assert.*;

import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;
import org.junit.Test;

public class StringLexerTest extends BaseLexerTest {
	
	@Test
	public void shouldSimpleStringBeRecognizedProperly() throws Exception {
		TokenStream tokenStream = buildTokenStream("'Hello World'");
		Token token = tokenStream.LT(1);
		assertEquals("Expected 'Hello World'", "'Hello World'", token.getText());
	}
	
	@Test
	public void shouldComplexStringBeRecognizedProperly() throws Exception {
		TokenStream tokenStream = buildTokenStream("'Hello \\'Angel\\' World'");
		Token token = tokenStream.LT(1);
		assertEquals("Expected 'Hello \\'Angel\\' World'", "'Hello \\'Angel\\' World'", token.getText());
	}
	
	@Test
	public void shouldIdentifierBeRecognizedProperly() throws Exception {
		TokenStream tokenStream = buildTokenStream("fileName1");
		Token token = tokenStream.LT(1);
		assertEquals("Expected valid identifier", "fileName1", token.getText());
	}
	
	@Test
	public void shouldRecognizeStringIdentifierStringWithSingleApostrophos() {
		TokenStream tokenStream = buildTokenStream("'Hello 'Angel' World'");
		Token token1 = tokenStream.LT(1);
		Token token2 = tokenStream.LT(2);
		Token token3 = tokenStream.LT(3);
		assertEquals("Invalid string","'Hello '", token1.getText());
		assertEquals("Invalid identifier","Angel", token2.getText());
		assertEquals("Invalid string","' World'", token3.getText());
	}

}
