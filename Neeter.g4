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
style_scope: (style_class|style_description|(style_class style_description)) '{' content+ '}';
style_class: '#' WORD;
style_description: '[' ']';
content: text | formula | style_scope | newline;
text: (WORD)+;
formula: '{{' (WORD)+ '}}';
newline: NEWLINE;

/*
 * lexer rules
 */

NEWLINE: '\n' WHITESPACE* '\n';

WHITESPACE
 : SPACES -> skip
 ;

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
 : [ \n\t]+
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