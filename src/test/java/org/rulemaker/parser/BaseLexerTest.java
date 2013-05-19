package org.rulemaker.parser;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.TokenStream;


public abstract class BaseLexerTest {
	
	protected TokenStream buildTokenStream(String sourceData) {
        CharStream charStream = new ANTLRStringStream(sourceData);
        RulesParserLexer lexer = new RulesParserLexer(charStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        return tokenStream;
    }

}
