lexer grammar NeeterLexer;

@header {
package com.neeter.grammar;
}

/*
 * lexer rules
 */

NEWLINE: NEWLINE_CHAR NEWLINE_CHAR+;

WHITESPACE : (SPACES|NEWLINE_CHAR) -> skip;

WORD: WORD_ITEM+;

OPEN_PAREN: '(';
CLOSE_PAREN: ')';
CLOSE_BRACE: '}';
DOUBLE_OPEN_BRACE: '{{';
DOUBLE_CLOSE_BRACE: '}}';
HASH: '#' -> mode(STYLE);

UNKNOWN_CHAR
 : .
 ;

/*
 * style
 */
mode STYLE;

ID: FIRST_ID_ITEM FOLLOWING_ID_ITEM*;
OPEN_BRACK: '[';
CLOSE_BRACK: ']';
EQUALS: '=';
COMMA: ',';
OPEN_BRACE: '{' -> mode(DEFAULT_MODE);
COLOR_VALUE: '#' HEX_DIGIT+;
INT_VALUE: DIGIT+;
STYLE_WHITESPACE : (SPACES | NEWLINE_CHAR) -> skip;

/*
 * fragments
 */

fragment SPACES
 : [ \t]+
 ;

fragment NEWLINE_CHAR
 : '\r'? '\n'
 ;

fragment FIRST_ID_ITEM : [A-Za-z_];
fragment FOLLOWING_ID_ITEM : [A-Za-z_0-9];

fragment HEX_DIGIT: [0-9a-fA-F];

fragment DIGIT: [0-9];

fragment WORD_ITEM
 : TEXT_CHAR
 | TEXT_ESCAPE_SEQ
 ;

fragment TEXT_CHAR
 : ~([ \n\t] | '\\' | '[' | ']' | '{' | '}' | '#')
 ;

fragment TEXT_ESCAPE_SEQ : '\\' . ;