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
import org.rulemaker.model.Condition;

}

@parser::members {

    private List<Term> currentConditionTermList;
    private Term.TermType currentTermType;
    private String currentTermValue;
    private List<Condition> conditionList = new ArrayList<Condition>();
    
    public List<Term> getCurrentConditionTermList() {
    	return currentConditionTermList;
    }
    
    public List<Condition> getConditionList() {
    	return conditionList;
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
	Term currentTerm = new Term($IDENTIFIER.text, currentTermType, currentTermValue);
	currentConditionTermList.add(currentTerm);
};

termList: term | term TERM_SEPARATOR termList;

condition
@init {
    currentConditionTermList = new ArrayList<Term>();    
}: LEFT_PAR termList RIGHT_PAR {
	Condition condition = new Condition(currentConditionTermList);
	conditionList.add(condition);
};

conditionList: condition EOF | condition conditionList EOF;