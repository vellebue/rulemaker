package org.rulemaker.parser;

import java.util.List;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.rulemaker.model.Rule;

public class RulesParser {
	
	private static final RulesParser instance = new RulesParser();
	 
	private RulesParser() {
	}
	
	public static RulesParser getInstance() {
		return instance;
	}
	
	public List<Rule> parseRules(String sourceData) throws ParserException {
		CharStream charStream = new ANTLRStringStream(sourceData);
        RulesParserLexer lexer = new RulesParserLexer(charStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        RulesParserParser parser = new RulesParserParser(tokenStream);
        try {
			parser.ruleSet();
		} catch (RecognitionException e) {
			throw new ParserException(e, sourceData);
		}
        return parser.getRuleList();
	}
}
