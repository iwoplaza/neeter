parser grammar PreeterParser;

@header {
package com.neeter.grammar;
}

options {tokenVocab = PreeterLexer;}

/*
 * parser rules
 */

program: (content|codeSnippet)+ EOF;
content: TEXT;
codeSnippet: '@{' (funcDef|statement)* '@}';

// Top-level code
funcDef: 'def' ID '(' idList? ')' '{' statement* '}';

// Statements
statement: codeScope|funcCall|variableDecl;
variableDecl: 'let' ID ('=' (literal|ID))? ';';
funcCall: ID '(' valueList? ')' ';';
codeScope: '{' statement* '}';

idList: ID (',' ID)*;
valueList: (literal|ID) (',' (literal|ID))*;
literal: STRING_LITERAL|INT_LITERAL;
