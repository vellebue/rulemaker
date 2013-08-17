package org.rulemaker.parser;

import static org.junit.Assert.assertEquals;

import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;
import org.junit.Test;

public class ExpressionLexerTest extends BaseLexerTest {
	
	@Test
	public void shouldRecognizeASimpleValidExpression() throws Exception {
		String expression = "${16 + 148 - a}";
		TokenStream tokenStream = buildTokenStream(expression);
		Token token = tokenStream.LT(1);
		assertEquals("${16 + 148 - a}", token.getText());
	}
	
	@Test
	public void shouldRecognizeAComplexValidExpression() throws Exception {
		String expression = "${new String[]{\"John\", \"Doe\"\\}[1]}";
		TokenStream tokenStream = buildTokenStream(expression);
		Token token = tokenStream.LT(1);
		assertEquals("${new String[]{\"John\", \"Doe\"\\}[1]}", token.getText());
	}

}
