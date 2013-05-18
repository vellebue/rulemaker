grammar RulesParser;

@lexer::header { 
package org.rulemaker.parser; 
}
@parser::header {
package org.rulemaker.parser;
}

//Lexer tokens

NUMBER: ('0'..'9')+('.'('0'..'9')*)?;
IDENTIFIER: ('a'..'z' | 'A'..'Z');


number: NUMBER;