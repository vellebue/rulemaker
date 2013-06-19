package org.rulemaker.parser;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;

/**
 * Base class for all parser tests.
 *
 * @author &Aacute;ngel Garc&iacute;a Bastanchuri
 *         Date: 14/05/12
 *         Time: 16:47
 */
public abstract class BaseParserTest {

    /**
     * Builds a function grammar parser for
     * given source data.
     * @param sourceData A source function expression to be tested.
     * @return A <code>FunctionGrammarParser</code> to process
     *         given source function expression.
     */
    protected RulesParserParser buildParser(String sourceData) {
        CharStream charStream = new ANTLRStringStream(sourceData);
        RulesParserLexer lexer = new RulesParserLexer(charStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        RulesParserParser parser = new RulesParserParser(tokenStream);
        return parser;
    }
}
