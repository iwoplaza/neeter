package com.neeter.preeter;

import com.neeter.grammar.PreeterParser;
import com.neeter.grammar.PreeterParserBaseListener;
import com.neeter.preeter.expression.*;
import com.neeter.preeter.statement.VariableAssignment;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class PreeterListener extends PreeterParserBaseListener
{
    private final FunctionRepository functionRepository;

    private final List<IPreeterNode> nodes = new ArrayList<>();

    private Stack<ICodeScope> scopeStack = new Stack<>();
    private Stack<IExpressionHost> expressionHostStack = new Stack<>();

    public PreeterListener(FunctionRepository functionRepository)
    {
        this.functionRepository = functionRepository;
    }

    public List<IPreeterNode> getNodes()
    {
        return nodes;
    }

    @Override
    public void enterCodeSnippet(PreeterParser.CodeSnippetContext ctx)
    {
        PreeterCodeNode codeNode = new PreeterCodeNode();
        nodes.add(codeNode);

        scopeStack.clear();
        scopeStack.push(codeNode);

        expressionHostStack.clear();
        expressionHostStack.push(codeNode);
    }

    @Override
    public void enterContent(PreeterParser.ContentContext ctx)
    {
        PreeterTextNode textNode = new PreeterTextNode(ctx.getText());
        nodes.add(textNode);
    }

    @Override
    public void enterFuncDef(PreeterParser.FuncDefContext ctx)
    {
        String funcId = ctx.ID().getText();

        List<String> parameterNames = ctx.idList().ID().stream().map(ParseTree::getText).collect(Collectors.toList());

        FunctionDefinition def = functionRepository.defineFunction(funcId, parameterNames);
        scopeStack.push(def);
        expressionHostStack.push(def);
    }

    @Override
    public void exitFuncDef(PreeterParser.FuncDefContext ctx)
    {
        scopeStack.pop();
        expressionHostStack.pop();
    }

    @Override
    public void enterCodeScope(PreeterParser.CodeScopeContext ctx)
    {
        CodeScope scope = new CodeScope();
        scopeStack.peek().receiveExpression(scope);
        scopeStack.push(scope);
        expressionHostStack.push(scope);
    }

    @Override
    public void exitCodeScope(PreeterParser.CodeScopeContext ctx)
    {
        scopeStack.pop();
        expressionHostStack.pop();
    }

    @Override
    public void enterVarDeclaration(PreeterParser.VarDeclarationContext ctx)
    {
        VariableAssignment variableDeclaration = new VariableAssignment(ctx.varId.getText(), true);
        scopeStack.peek().addStatement(variableDeclaration);

        expressionHostStack.push(variableDeclaration);
    }

    @Override
    public void exitVarDeclaration(PreeterParser.VarDeclarationContext ctx)
    {
        expressionHostStack.pop();
    }

    @Override
    public void enterVarAssignment(PreeterParser.VarAssignmentContext ctx)
    {
        VariableAssignment variableDeclaration = new VariableAssignment(ctx.varId.getText(), false);
        scopeStack.peek().addStatement(variableDeclaration);

        expressionHostStack.push(variableDeclaration);
    }

    @Override
    public void exitVarAssignment(PreeterParser.VarAssignmentContext ctx)
    {
        expressionHostStack.pop();
    }

    @Override
    public void enterLiteralExpr(PreeterParser.LiteralExprContext ctx)
    {
        assert(expressionHostStack.peek() != null);

        if (ctx.literal().INT_LITERAL() != null)
        {
            expressionHostStack.peek().receiveExpression(new LiteralExpression(Integer.parseInt(ctx.literal().INT_LITERAL().getText())));
        }
        else if (ctx.literal().STRING_LITERAL() != null)
        {
            String text = ctx.literal().STRING_LITERAL().getText();
            text = LiteralHelper.processStringLiteral(text);
            expressionHostStack.peek().receiveExpression(new LiteralExpression(text));
        }
        else
        {
            throw new PreeterCompileError(String.format("Found literal of unsupported type: %s", ctx.getText()));
        }
    }

    @Override
    public void enterIdentifierExpr(PreeterParser.IdentifierExprContext ctx)
    {
        IdentifierExpression op = new IdentifierExpression(ctx.getText());
        expressionHostStack.peek().receiveExpression(op);
    }

    @Override
    public void enterMultiplyExpr(PreeterParser.MultiplyExprContext ctx)
    {
        BinaryOperation op = new BinaryOperation((c, a, b) -> (int)a.evaluate(c) * (int)b.evaluate(c));
        expressionHostStack.peek().receiveExpression(op);
        expressionHostStack.push(op);
    }

    @Override
    public void exitMultiplyExpr(PreeterParser.MultiplyExprContext ctx)
    {
        expressionHostStack.pop();
    }

    @Override
    public void enterAddExpr(PreeterParser.AddExprContext ctx)
    {
        BinaryOperation op = new BinaryOperation((c, a, b) -> (int)a.evaluate(c) + (int)b.evaluate(c));
        expressionHostStack.peek().receiveExpression(op);
        expressionHostStack.push(op);
    }

    @Override
    public void exitAddExpr(PreeterParser.AddExprContext ctx)
    {
        expressionHostStack.pop();
    }

    @Override
    public void enterSubtractExpr(PreeterParser.SubtractExprContext ctx)
    {
        BinaryOperation op = new BinaryOperation((c, a, b) -> (int)a.evaluate(c) - (int)b.evaluate(c));
        expressionHostStack.peek().receiveExpression(op);
        expressionHostStack.push(op);
    }

    @Override
    public void exitSubtractExpr(PreeterParser.SubtractExprContext ctx)
    {
        expressionHostStack.pop();
    }

    @Override
    public void enterDivideExpr(PreeterParser.DivideExprContext ctx)
    {
        BinaryOperation op = new BinaryOperation((c, a, b) -> (int)a.evaluate(c) / (int)b.evaluate(c));
        expressionHostStack.peek().receiveExpression(op);
        expressionHostStack.push(op);
    }

    @Override
    public void exitDivideExpr(PreeterParser.DivideExprContext ctx)
    {
        expressionHostStack.pop();
    }

    @Override
    public void enterFuncCallExpr(PreeterParser.FuncCallExprContext ctx)
    {
        FunctionCall functionCall = new FunctionCall(ctx.funcCall().ID().getText());
        expressionHostStack.peek().receiveExpression(functionCall);
        expressionHostStack.push(functionCall);
    }

    @Override
    public void exitFuncCallExpr(PreeterParser.FuncCallExprContext ctx)
    {
        expressionHostStack.pop();
    }
}
