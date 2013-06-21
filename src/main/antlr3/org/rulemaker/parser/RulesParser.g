grammar RulesParser;

options {
    backtrack = true;
}

@lexer::header { 
package org.rulemaker.parser; 
}

@parser::header {
package org.rulemaker.parser;

import java.util.List;
import java.util.ArrayList;

import org.rulemaker.model.Term;

}

@parser::members {
    List<Term> currentConditionTermList;
    Term.TermType currentTermType;
    String currentTermValue;
    
    public List<Term> getCurrentConditionTermList() {
    	return currentConditionTermList;
    }
}

//Lexer tokens

NUMBER: ('0'..'9')+('.'('0'..'9')*)?;
IDENTIFIER: ('a'..'z' | 'A'..'Z')('a'..'z' | 'A'..'Z' | '0'..'9')*;
LEFT_PAR: '(';
RIGHT_PAR: ')';
EQUAL: '=';
STRING: '\'' (~('\'')|('\\\''))* '\'';
TERM_SEPARATOR: ',';

//Parser tokens

expression: NUMBER {
               currentTermType = Term.TermType.NUMBER;
               currentTermValue = $NUMBER.text;
            } | IDENTIFIER {
               currentTermType = Term.TermType.IDENTIFIER;
               currentTermValue = $IDENTIFIER.text;
            } | STRING {
               currentTermType = Term.TermType.STRING;
               currentTermValue = $STRING.text.substring(1, $STRING.text.length() - 1);
            };

term: IDENTIFIER EQUAL expression {

	Term currentTerm = new Term();
	currentTerm.setIdentifier($IDENTIFIER.text);
	currentTerm.setExpressionType(currentTermType);
	currentTerm.setExpressionValue(currentTermValue);
	currentConditionTermList.add(currentTerm);
};

termList: term | term TERM_SEPARATOR termList;

condition
@init {
    
    currentConditionTermList = new ArrayList<Term>();
    
}: LEFT_PAR termList RIGHT_PAR;