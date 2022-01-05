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
codeSnippet: '@{' (funcDef|instruction)* '@}';
instruction: statement|expr;

// Top-level code
funcDef: 'def' ID '(' idList? ')' '{' instruction* '}';

// Statements
statement: codeScope|varDeclaration|whileStatement|ifStatement;
codeScope: '{' instruction* '}';
varDeclaration: 'let' varId=ID ('=' expr)? ';';

whileStatement: 'while' '(' condition=expr ')' statementBody;
ifStatement: 'if' '(' condition=expr ')'
   mainBody=statementBody
   ('else' elseBody=statementBody)?;
statementBody
    : instruction
    | '{' instruction* '}'
    ;

// Expressions
// (Precedence goes from top to bottom)
expr
  : ID # IdentifierExpr
  | literal # LiteralExpr
  | funcCall # FuncCallExpr
  | varAssignment # AssignExpr
  | '(' expr ')' # BoundExpr
  | expr '*' expr # MultiplyExpr
  | expr '/' expr # DivideExpr
  | expr '%' expr # ModExpr
  | expr '+' expr # AddExpr
  | expr '-' expr # SubtractExpr
  | expr '<=' expr # LessEqExpr
  | expr '<' expr # LessExpr
  | expr '>' expr # MoreExpr
  | expr '>=' expr # MoreEqExpr
  | expr '==' expr # EqExpr
  ;

varAssignment: varId=ID '=' expr ';';
funcCall: ID '(' valueList? ')' ';';
idList: ID (',' ID)*;
valueList: expr (',' expr)*;
literal: STRING_LITERAL|INT_LITERAL;
