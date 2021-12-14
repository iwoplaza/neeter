package com.neeter.preeter;

import com.neeter.grammar.PreeterParser;
import com.neeter.grammar.PreeterParserBaseListener;
import com.neeter.preeter.expression.*;
import com.neeter.preeter.expression.VariableAssignment;
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
    }

    @Override
    public void exitFuncDef(PreeterParser.FuncDefContext ctx)
    {
        scopeStack.pop();
    }

    @Override
    public void enterCodeScope(PreeterParser.CodeScopeContext ctx)
    {
        CodeScope scope = new CodeScope();
        scopeStack.peek().receiveExpression(scope);
        scopeStack.push(scope);
    }

    @Override
    public void exitCodeScope(PreeterParser.CodeScopeContext ctx)
    {
        scopeStack.pop();
    }

    @Override
    public void enterVarDeclaration(PreeterParser.VarDeclarationContext ctx)
    {
        VariableAssignment variableDeclaration = new VariableAssignment(ctx.varId.getText(), true);
        scopeStack.peek().receiveExpression(variableDeclaration);
        scopeStack.push(variableDeclaration);
    }

    @Override
    public void exitVarDeclaration(PreeterParser.VarDeclarationContext ctx)
    {
        scopeStack.pop();
    }

    @Override
    public void enterVarAssignment(PreeterParser.VarAssignmentContext ctx)
    {
        VariableAssignment variableDeclaration = new VariableAssignment(ctx.varId.getText(), false);
        scopeStack.peek().receiveExpression(variableDeclaration);
        scopeStack.push(variableDeclaration);
    }

    @Override
    public void exitVarAssignment(PreeterParser.VarAssignmentContext ctx)
    {
        scopeStack.pop();
    }

    @Override
    public void enterLiteralExpr(PreeterParser.LiteralExprContext ctx)
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
    }

    @Override
    public void enterIdentifierExpr(PreeterParser.IdentifierExprContext ctx)
    {
        IdentifierExpression op = new IdentifierExpression(ctx.getText());
        scopeStack.peek().receiveExpression(op);
    }

    @Override
    public void enterMultiplyExpr(PreeterParser.MultiplyExprContext ctx)
    {
        BinaryOperation op = new BinaryOperation((c, a, b) -> (int)a.evaluate(c) * (int)b.evaluate(c));
        scopeStack.peek().receiveExpression(op);
        scopeStack.push(op);
    }

    @Override
    public void exitMultiplyExpr(PreeterParser.MultiplyExprContext ctx)
    {
        scopeStack.pop();
    }

    @Override
    public void enterAddExpr(PreeterParser.AddExprContext ctx)
    {
        BinaryOperation op = new BinaryOperation((c, a, b) -> (int)a.evaluate(c) + (int)b.evaluate(c));
        scopeStack.peek().receiveExpression(op);
        scopeStack.push(op);
    }

    @Override
    public void exitAddExpr(PreeterParser.AddExprContext ctx)
    {
        scopeStack.pop();
    }

    @Override
    public void enterSubtractExpr(PreeterParser.SubtractExprContext ctx)
    {
        BinaryOperation op = new BinaryOperation((c, a, b) -> (int)a.evaluate(c) - (int)b.evaluate(c));
        scopeStack.peek().receiveExpression(op);
        scopeStack.push(op);
    }

    @Override
    public void exitSubtractExpr(PreeterParser.SubtractExprContext ctx)
    {
        scopeStack.pop();
    }

    @Override
    public void enterDivideExpr(PreeterParser.DivideExprContext ctx)
    {
        BinaryOperation op = new BinaryOperation((c, a, b) -> (int)a.evaluate(c) / (int)b.evaluate(c));
        scopeStack.peek().receiveExpression(op);
        scopeStack.push(op);
    }

    @Override
    public void exitDivideExpr(PreeterParser.DivideExprContext ctx)
    {
        scopeStack.pop();
    }

    @Override
    public void enterFuncCallExpr(PreeterParser.FuncCallExprContext ctx)
    {
        FunctionCall functionCall = new FunctionCall(ctx.funcCall().ID().getText());
        scopeStack.peek().receiveExpression(functionCall);
        scopeStack.push(functionCall);
    }

    @Override
    public void exitFuncCallExpr(PreeterParser.FuncCallExprContext ctx)
    {
        scopeStack.pop();
    }
}
