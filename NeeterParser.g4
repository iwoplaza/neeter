parser grammar NeeterParser;

@header {
package com.neeter.grammar;
}

options {tokenVocab = NeeterLexer;}

/*
 * parser rules
 */

program: content+ EOF;
styleScope: '#' (styleClass=ID|styleDescription|(styleClass=ID styleDescription)) '{' content+ '}';
styleDescription: '[' styleProperty (',' styleProperty)* ']';
styleProperty: propKey=ID '=' (idValue=ID|colorValue=COLOR_VALUE|intValue=INT_VALUE);
content: text | formula | styleScope | newline;
text: (WORD)+;
formula: '{{' (WORD)+ '}}';
newline: NEWLINE;