lexer grammar PreeterLexer;

@header {
package com.neeter.grammar;
}

TEXT: TEXT_ITEM+;
CODE_START: '@{' -> mode(CODE);

mode CODE;

DEF: 'def';
LET: 'let';
ID: ID_ITEM+;
OPEN_PAREN: '(';
CLOSE_PAREN: ')';
OPEN_BRACE: '{';
CLOSE_BRACE: '}';
SEMICOLON: ';';
COMMA: ',';
EQUALS: '=';
PLUS: '+';
MINUS: '-';
STAR: '*';
FRONT_SLASH: '/';
WHITESPACE : (SPACES | NEWLINE_CHAR) -> skip;
STRING_LITERAL: '"' ~('"')* '"';
INT_LITERAL: [0-9]+;

CODE_END: '@}' -> mode(DEFAULT_MODE);

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

fragment TEXT_ITEM
 : TEXT_CHAR
 | TEXT_ESCAPE_SEQ
 ;

fragment TEXT_CHAR
 : ~'@'
 ;

fragment TEXT_ESCAPE_SEQ : '\\' . ;