grammar Neeter;

tokens { INDENT, DEDENT }

@header {
package com.neeter.grammar;
}

@members {
boolean gathering_class_id = false;
}

/*
 * parser rules
 */

program: content+ EOF;
style_scope: (style_class|style_description|(style_class style_description)) '{' content+ '}';
style_class: '#' WORD;
style_description: '[' ']';
content: text | formula | style_scope;
text: (WORD)+;
formula: '{{' (WORD)+ '}}';

/*
 * lexer rules
 */

WORD: WORD_ITEM+;

WHITESPACE
 : SPACES -> skip
 ;

OPEN_PAREN: '(';
CLOSE_PAREN: ')';
OPEN_BRACK: '[';
CLOSE_BRACK: ']';
OPEN_BRACE: '{';
CLOSE_BRACE: '}';
DOUBLE_OPEN_BRACE: '{{';
DOUBLE_CLOSE_BRACE: '}}';
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

fragment COMMENT
 : '//' ~[\r\n\f]*
 ;

fragment ID_ITEM : [A-Za-z_];

fragment WORD_ITEM
 : TEXT_CHAR
 | TEXT_ESCAPE_SEQ
 ;

fragment TEXT_CHAR
 : ~([ \t] | '\\' | '[' | ']' | '{' | '}' | '#')
 ;

fragment TEXT_ESCAPE_SEQ : '\\' . ;