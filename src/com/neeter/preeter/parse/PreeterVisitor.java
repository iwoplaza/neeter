package com.neeter.preeter.parse;

import com.neeter.grammar.PreeterParser;
import com.neeter.grammar.PreeterParserBaseVisitor;
import com.neeter.preeter.*;
import com.neeter.preeter.expression.*;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PreeterVisitor extends PreeterParserBaseVisitor<IExpression>
{
    private final FunctionRepository functionRepository;
    private final List<IPreeterNode> nodes = new ArrayList<>();

    public PreeterVisitor(FunctionRepository functionRepository)
    {
        this.functionRepository = functionRepository;
    }

    public List<IPreeterNode> getNodes()
    {
        return nodes;
    }

    @Override
    public IExpression visitCodeSnippet(PreeterParser.CodeSnippetContext ctx)
    {
        // Visiting each function definition.
        ctx.funcDef().forEach(this::visit);

        List<IExpression> statements = ctx.instruction().stream().map(this::visit).collect(Collectors.toList());
        nodes.add(new PreeterCodeNode(ctx.start.getLine(), statements));

        return null;
    }

    @Override
    public IExpression visitContent(PreeterParser.ContentContext ctx)
    {
        PreeterTextNode textNode = new PreeterTextNode(ctx.getText());
        nodes.add(textNode);

        return null;
    }

    @Override
    public IExpression visitInstruction(PreeterParser.InstructionContext ctx)
    {
        PreeterParser.StatementContext statementContext = ctx.statement();
        PreeterParser.ExprContext exprContext = ctx.expr();

        if (statementContext != null)
        {
            return visit(statementContext);
        }
        else if (exprContext != null)
        {
            return visit(exprContext);
        }

        return null;
    }

    @Override
    public IExpression visitFuncDef(PreeterParser.FuncDefContext ctx)
    {
        String funcId = ctx.ID().getText();

        List<String> parameterNames = new ArrayList<>();

        PreeterParser.IdListContext idList = ctx.idList();
        if (idList != null)
        {
            parameterNames = idList.ID().stream().map(ParseTree::getText).collect(Collectors.toList());
        }

        List<IExpression> statements = ctx.instruction().stream().map(this::visit).collect(Collectors.toList());
        functionRepository.defineFunction(DocContext.fromToken(ctx.start), funcId, parameterNames, statements);

        return null;
    }

    @Override
    public IExpression visitVarDeclaration(PreeterParser.VarDeclarationContext ctx)
    {
        return new VariableAssignment(ctx.start.getLine(), ctx.varId.getText(), true, visit(ctx.expr()));
    }

    @Override
    public IExpression visitVarAssignment(PreeterParser.VarAssignmentContext ctx)
    {
        return new VariableAssignment(ctx.start.getLine(), ctx.varId.getText(), false, visit(ctx.expr()));
    }

    @Override
    public IExpression visitCodeScope(PreeterParser.CodeScopeContext ctx)
    {
        List<IExpression> statements = ctx.instruction().stream().map(this::visit).collect(Collectors.toList());

        return new CodeScope(ctx.start.getLine(), statements);
    }

    @Override
    public IExpression visitWhileStatement(PreeterParser.WhileStatementContext ctx)
    {
        IExpression conditionExpression = visit(ctx.condition);
        List<IExpression> statements = ctx.statementBody().instruction().stream().map(this::visit).collect(Collectors.toList());

        return new WhileLoop(ctx.start.getLine(), conditionExpression, statements);
    }

    @Override
    public IExpression visitIfStatement(PreeterParser.IfStatementContext ctx)
    {
        IExpression conditionExpression = visit(ctx.condition);
        List<IExpression> statements = ctx.mainBody.instruction().stream().map(this::visit).collect(Collectors.toList());
        List<IExpression> elseStatements = null;

        if (ctx.elseBody != null)
        {
            elseStatements = ctx.elseBody.instruction().stream().map(this::visit).collect(Collectors.toList());
        }

        return new IfStatement(DocContext.fromToken(ctx.start), conditionExpression, statements, elseStatements);
    }

    @Override
    public IExpression visitReturnStatement(PreeterParser.ReturnStatementContext ctx)
    {
        PreeterParser.ExprContext exprContext = ctx.expr();
        IExpression valueExpression = exprContext != null ? visit(exprContext) : null;

        return new ReturnStatement(DocContext.fromToken(ctx.start), valueExpression);
    }

    @Override
    public IExpression visitBreakStatement(PreeterParser.BreakStatementContext ctx)
    {
        return new BreakStatement(DocContext.fromToken(ctx.start));
    }

    @Override
    public IExpression visitContinueStatement(PreeterParser.ContinueStatementContext ctx)
    {
        return new ContinueStatement(DocContext.fromToken(ctx.start));
    }

    @Override
    public IExpression visitLiteralExpr(PreeterParser.LiteralExprContext ctx)
    {
        DocContext docContext = DocContext.fromToken(ctx.start);

        if (ctx.literal().INT_LITERAL() != null)
        {
            return new LiteralExpression(docContext, Integer.parseInt(ctx.literal().INT_LITERAL().getText()));
        }
        else if (ctx.literal().STRING_LITERAL() != null)
        {
            String text = ctx.literal().STRING_LITERAL().getText();
            text = LiteralHelper.processStringLiteral(text);
            return new LiteralExpression(docContext, text);
        }
        else if (ctx.literal().TRUE_LITERAL() != null)
        {
            return new LiteralExpression(docContext, true);
        }
        else if (ctx.literal().FALSE_LITERAL() != null)
        {
            return new LiteralExpression(docContext, false);
        }

        throw new PreeterCompileError(String.format("Found literal of unsupported type: %s", ctx.getText()), docContext);
    }

    @Override
    public IExpression visitIdentifierExpr(PreeterParser.IdentifierExprContext ctx)
    {
        return new IdentifierExpression(DocContext.fromToken(ctx.start), ctx.getText());
    }

    @Override
    public IExpression visitBoundExpr(PreeterParser.BoundExprContext ctx)
    {
        return visit(ctx.expr());
    }

    @Override
    public IExpression visitMultiplyExpr(PreeterParser.MultiplyExprContext ctx)
    {
        return new BinaryOperation(DocContext.fromToken(ctx.start), Operations.MULTIPLY, visit(ctx.expr(0)), visit(ctx.expr(1)));
    }

    @Override
    public IExpression visitAddExpr(PreeterParser.AddExprContext ctx)
    {
        return new BinaryOperation(DocContext.fromToken(ctx.start), Operations.ADD, visit(ctx.expr(0)), visit(ctx.expr(1)));
    }

    @Override
    public IExpression visitSubtractExpr(PreeterParser.SubtractExprContext ctx)
    {
        return new BinaryOperation(DocContext.fromToken(ctx.start), Operations.SUBTRACT, visit(ctx.expr(0)), visit(ctx.expr(1)));
    }

    @Override
    public IExpression visitDivideExpr(PreeterParser.DivideExprContext ctx)
    {
        return new BinaryOperation(DocContext.fromToken(ctx.start), Operations.DIVIDE, visit(ctx.expr(0)), visit(ctx.expr(1)));
    }

    @Override
    public IExpression visitModExpr(PreeterParser.ModExprContext ctx)
    {
        return new BinaryOperation(DocContext.fromToken(ctx.start), Operations.MODULO, visit(ctx.expr(0)), visit(ctx.expr(1)));
    }

    @Override
    public IExpression visitEqExpr(PreeterParser.EqExprContext ctx)
    {
        return new BinaryOperation(DocContext.fromToken(ctx.start), Operations.EQUALS, visit(ctx.expr(0)), visit(ctx.expr(1)));
    }

    @Override
    public IExpression visitMoreEqExpr(PreeterParser.MoreEqExprContext ctx)
    {
        return new BinaryOperation(DocContext.fromToken(ctx.start), Operations.MORE_EQUAL, visit(ctx.expr(0)), visit(ctx.expr(1)));
    }

    @Override
    public IExpression visitLessEqExpr(PreeterParser.LessEqExprContext ctx)
    {
        return new BinaryOperation(DocContext.fromToken(ctx.start), Operations.LESS_EQUAL, visit(ctx.expr(0)), visit(ctx.expr(1)));
    }

    @Override
    public IExpression visitMoreExpr(PreeterParser.MoreExprContext ctx)
    {
        return new BinaryOperation(DocContext.fromToken(ctx.start), Operations.MORE_THAN, visit(ctx.expr(0)), visit(ctx.expr(1)));
    }

    @Override
    public IExpression visitLessExpr(PreeterParser.LessExprContext ctx)
    {
        return new BinaryOperation(DocContext.fromToken(ctx.start), Operations.LESS_THAN, visit(ctx.expr(0)), visit(ctx.expr(1)));
    }

    @Override
    public IExpression visitAndExpr(PreeterParser.AndExprContext ctx)
    {
        return new BinaryOperation(DocContext.fromToken(ctx.start), Operations.AND, visit(ctx.expr(0)), visit(ctx.expr(1)));
    }

    @Override
    public IExpression visitOrExpr(PreeterParser.OrExprContext ctx)
    {
        return new BinaryOperation(DocContext.fromToken(ctx.start), Operations.OR, visit(ctx.expr(0)), visit(ctx.expr(1)));
    }

    @Override
    public IExpression visitFuncCallExpr(PreeterParser.FuncCallExprContext ctx)
    {
        PreeterParser.ValueListContext valueList = ctx.funcCall().valueList();

        List<IExpression> argumentList;
        if (valueList != null)
        {
            argumentList = ctx.funcCall().valueList().expr().stream()
                    .map(this::visit).collect(Collectors.toList());
        }
        else
        {
            argumentList = new ArrayList<>();
        }

        return new FunctionCall(DocContext.fromToken(ctx.start), ctx.funcCall().ID().getText(), argumentList);
    }
}
