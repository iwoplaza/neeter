package com.neeter.preeter;

import com.neeter.grammar.PreeterParser;
import com.neeter.grammar.PreeterParserBaseVisitor;
import com.neeter.preeter.expression.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class PreeterVisitor extends PreeterParserBaseVisitor<PreeterVisitor.Result>
{
    private final FunctionRepository functionRepository;
    private final List<IPreeterNode> nodes = new ArrayList<>();
    private Stack<ICodeScope> scopeStack = new Stack<>();

    public PreeterVisitor(FunctionRepository functionRepository)
    {
        this.functionRepository = functionRepository;
    }

    public List<IPreeterNode> getNodes()
    {
        return nodes;
    }

    @Override
    public Result visitCodeSnippet(PreeterParser.CodeSnippetContext ctx)
    {
        PreeterCodeNode codeNode = new PreeterCodeNode();
        nodes.add(codeNode);

        scopeStack.push(codeNode);
        visitChildren(ctx);
        scopeStack.pop();

        return null;
    }

    @Override
    public Result visitContent(PreeterParser.ContentContext ctx)
    {
        PreeterTextNode textNode = new PreeterTextNode(ctx.getText());
        nodes.add(textNode);

        // Should have no children
        return null;
    }

    @Override
    public Result visitFuncDef(PreeterParser.FuncDefContext ctx)
    {
        String funcId = ctx.ID().getText();

        List<String> parameterNames = ctx.idList().ID().stream().map(ParseTree::getText).collect(Collectors.toList());
        FunctionDefinition def = functionRepository.defineFunction(funcId, parameterNames);

        scopeStack.push(def);
        visitChildren(ctx);
        scopeStack.pop();

        return null;
    }

    @Override
    public Result visitVarAssignment(PreeterParser.VarAssignmentContext ctx)
    {
        VariableAssignment variableDeclaration = new VariableAssignment(ctx.varId.getText(), false);
        scopeStack.peek().receiveExpression(variableDeclaration);

        scopeStack.push(variableDeclaration);
        visitChildren(ctx);
        scopeStack.pop();

        return null;
    }

    @Override
    public Result visitLiteralExpr(PreeterParser.LiteralExprContext ctx)
    {
        assert(scopeStack.peek() != null);

        if (ctx.literal().INT_LITERAL() != null)
        {
            scopeStack.peek().receiveExpression(new LiteralExpression(Integer.parseInt(ctx.literal().INT_LITERAL().getText())));
        }
        else if (ctx.literal().STRING_LITERAL() != null)
        {
            String text = ctx.literal().STRING_LITERAL().getText();
            text = LiteralHelper.processStringLiteral(text);
            scopeStack.peek().receiveExpression(new LiteralExpression(text));
        }
        else
        {
            throw new PreeterCompileError(String.format("Found literal of unsupported type: %s", ctx.getText()));
        }

        return null;
    }

    @Override
    public Result visitIdentifierExpr(PreeterParser.IdentifierExprContext ctx)
    {
        IdentifierExpression op = new IdentifierExpression(ctx.getText());
        scopeStack.peek().receiveExpression(op);

        return null;
    }

    @Override
    public Result visitCodeScope(PreeterParser.CodeScopeContext ctx)
    {
        return handleExpressionHost(ctx, new CodeScope());
    }

    @Override
    public Result visitWhileStatement(PreeterParser.WhileStatementContext ctx)
    {
        return handleExpressionHost(ctx, new WhileLoop());
    }

    @Override
    public Result visitIfStatement(PreeterParser.IfStatementContext ctx)
    {
        IfStatement statement = new IfStatement();
        scopeStack.peek().receiveExpression(statement);

        
        visit(ctx.expr());

        scopeStack.push(statement);
        visitChildren(ctx);
        scopeStack.pop();

        return null;
    }

    @Override
    public Result visitVarDeclaration(PreeterParser.VarDeclarationContext ctx)
    {
        return handleExpressionHost(ctx, new VariableAssignment(ctx.varId.getText(), true));
    }

    @Override
    public Result visitMultiplyExpr(PreeterParser.MultiplyExprContext ctx)
    {
        return handleExpressionHost(ctx, new BinaryOperation(Operations.MULTIPLY));
    }

    @Override
    public Result visitAddExpr(PreeterParser.AddExprContext ctx)
    {
        return handleExpressionHost(ctx, new BinaryOperation(Operations.ADD));
    }

    @Override
    public Result visitSubtractExpr(PreeterParser.SubtractExprContext ctx)
    {
        return handleExpressionHost(ctx, new BinaryOperation(Operations.SUBTRACT));
    }

    @Override
    public Result visitDivideExpr(PreeterParser.DivideExprContext ctx)
    {
        return handleExpressionHost(ctx, new BinaryOperation(Operations.DIVIDE));
    }

    @Override
    public Result visitEqExpr(PreeterParser.EqExprContext ctx)
    {
        return handleExpressionHost(ctx, new BinaryOperation(Operations.EQUALS));
    }

    @Override
    public Result visitMoreEqExpr(PreeterParser.MoreEqExprContext ctx)
    {
        return handleExpressionHost(ctx, new BinaryOperation(Operations.MORE_EQUAL));
    }

    @Override
    public Result visitLessEqExpr(PreeterParser.LessEqExprContext ctx)
    {
        return handleExpressionHost(ctx, new BinaryOperation(Operations.LESS_EQUAL));
    }

    @Override
    public Result visitMoreExpr(PreeterParser.MoreExprContext ctx)
    {
        return handleExpressionHost(ctx, new BinaryOperation(Operations.MORE_THAN));
    }

    @Override
    public Result visitLessExpr(PreeterParser.LessExprContext ctx)
    {
        return handleExpressionHost(ctx, new BinaryOperation(Operations.LESS_THAN));
    }

    @Override
    public Result visitFuncCallExpr(PreeterParser.FuncCallExprContext ctx)
    {
        return handleExpressionHost(ctx, new FunctionCall(ctx.funcCall().ID().getText()));
    }

    private <T extends RuleNode> Result handleExpressionHost(T ctx, ICodeScope expression)
    {
        scopeStack.peek().receiveExpression(expression);

        scopeStack.push(expression);
        visitChildren(ctx);
        scopeStack.pop();

        return null;
    }

    public static class Result
    {
    }
}
