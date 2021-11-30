grammar Neeter;

@header {
package com.neeter.grammar;
}

@members {
boolean gatheringClassId = false;
boolean insideFormula = false;
}

/*
 * parser rules
 */

program: content+ EOF;
styleScope: (styleClass|styleDescription|(styleClass styleDescription)) '{' content+ '}';
styleClass: '#' WORD;
styleDescription: '[' ']';
content: text | formula | styleScope | newline;
text: (WORD)+;
formula: '{{' (WORD)+ '}}';
newline: NEWLINE;

/*
 * lexer rules
 */

NEWLINE: NEWLINE_CHAR NEWLINE_CHAR+;

WHITESPACE : (SPACES|NEWLINE_CHAR) -> skip;

WORD: WORD_ITEM+;

OPEN_PAREN: '(';
CLOSE_PAREN: ')';
OPEN_BRACK: '[';
CLOSE_BRACK: ']';
OPEN_BRACE: '{';
CLOSE_BRACE: '}';
DOUBLE_OPEN_BRACE: '{{' {
    insideFormula = true;
};
DOUBLE_CLOSE_BRACE: '}}'  {
    insideFormula = false;
};
HASH: '#';

UNKNOWN_CHAR
 : .
 ;

/*
 * fragments
 */

fragment SPACES
 : [ \t]+
 ;

fragment NEWLINE_CHAR
 : '\r'? '\n'
 ;

fragment COMMENT
 : '//' ~[\r\n\f]*
 ;

fragment ID_ITEM : [A-Za-z_];

fragment WORD_ITEM
 : TEXT_CHAR
 | TEXT_ESCAPE_SEQ
 ;

fragment TEXT_CHAR
 : ~([ \n\t] | '\\' | '[' | ']' | '{' | '}' | '#')
 ;

fragment TEXT_ESCAPE_SEQ : '\\' . ;