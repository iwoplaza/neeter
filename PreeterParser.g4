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
codeSnippet: '@{' (funcDef|statement|expr)* '@}';

// Top-level code
funcDef: 'def' ID '(' idList? ')' '{' (statement|expr)* '}';

// Statements
statement: codeScope|varDeclaration|varAssignment;
varDeclaration: 'let' varId=ID ('=' expr)? ';';
varAssignment: varId=ID ('=' expr)? ';';
codeScope: '{' (statement|expr)* '}';

// Expressions
expr
  : ID # IdentifierExpr
  | literal # LiteralExpr
  | funcCall # FuncCallExpr
  | expr '+' expr # AddExpr
  | expr '-' expr # SubtractExpr
  | expr '*' expr # MultiplyExpr
  | expr '/' expr # DivideExpr
  ;

funcCall: ID '(' valueList? ')' ';';
idList: ID (',' ID)*;
valueList: expr (',' expr)*;
literal: STRING_LITERAL|INT_LITERAL;
