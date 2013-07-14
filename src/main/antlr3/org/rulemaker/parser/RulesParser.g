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
import org.rulemaker.model.Action;
import org.rulemaker.model.Rule;

}

@parser::members {

    private List<Term> currentTokenTermList;
    private Term.TermType currentTermType;
    private String currentTermValue;
    private List<Condition> conditionList = new ArrayList<Condition>();
    private List<Action> actionList = new ArrayList<Action>();
    private Rule rule;
    private List<Rule> ruleList = new ArrayList<Rule>();
    
    public List<Term> getCurrentTokenTermList() {
    	return currentTokenTermList;
    }
    
    public List<Condition> getConditionList() {
    	return conditionList;
    }
    
    public List<Action> getActionList() {
    	return actionList;
    }
    
    public Rule getRule() {
    	return rule;
    }
    
    public List<Rule> getRuleList() {
    	return ruleList;
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
RULE_SEPARATOR: '->';
SEMICOLON: ';';

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
	currentTokenTermList.add(currentTerm);
};

termList: term | term TERM_SEPARATOR termList;

condition
@init {
    currentTokenTermList = new ArrayList<Term>();    
}: LEFT_PAR termList RIGHT_PAR {
	Condition condition = new Condition(currentTokenTermList);
	conditionList.add(condition);
};

conditionList: condition (| conditionList);

action
@init {
	currentTokenTermList = new ArrayList<Term>();
}: IDENTIFIER LEFT_PAR termList RIGHT_PAR {
	Action action = new Action($IDENTIFIER.text, currentTokenTermList);
	actionList.add(action);
};

actionList: action (| actionList);

rule: RULE_SEPARATOR actionList {
		rule = new Rule(new ArrayList<Condition>(), actionList);
		conditionList = new ArrayList<Condition>();
		actionList = new ArrayList<Action>();
		ruleList.add(rule);
	} 
	| conditionList RULE_SEPARATOR actionList {
		rule = new Rule(conditionList, actionList);
		conditionList = new ArrayList<Condition>();
		actionList = new ArrayList<Action>();
		ruleList.add(rule);
    };
    
ruleSet: rule EOF {
		 //  ruleList.add(rule);
	   }
	   | rule SEMICOLON ruleSet EOF {
	   	 //  ruleList.add(rule);
	   };