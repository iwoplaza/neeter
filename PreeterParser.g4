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
statement: codeScope|varDeclaration|whileStatement|ifStatement;
codeScope: '{' (statement|expr)* '}';
varDeclaration: 'let' varId=ID ('=' expr)? ';';

whileStatement: 'while' '(' expr ')' statementBody;
ifStatement: 'if' '(' expr ')'
   mainBody=statementBody
   ('else' elseBody=statementBody)?;
statementBody
    : (statement|expr)
    | '{' (statement|expr)* '}'
    ;

// Expressions
expr
  : ID # IdentifierExpr
  | literal # LiteralExpr
  | funcCall # FuncCallExpr
  | varAssignment # AssignExpr
  | expr '+' expr # AddExpr
  | expr '-' expr # SubtractExpr
  | expr '*' expr # MultiplyExpr
  | expr '/' expr # DivideExpr
  | expr '==' expr # EqExpr
  | expr '<=' expr # LessEqExpr
  | expr '<' expr # LessExpr
  | expr '>' expr # MoreExpr
  | expr '>=' expr # MoreEqExpr
  ;

varAssignment: varId=ID ('=' expr)? ';';
funcCall: ID '(' valueList? ')' ';';
idList: ID (',' ID)*;
valueList: expr (',' expr)*;
literal: STRING_LITERAL|INT_LITERAL;
