package com.neeter.preeter.expression;

import com.neeter.preeter.execution.IExecutionContext;
import com.neeter.preeter.parse.DocContext;

public class BinaryOperation extends ExpressionBase
{
    private final EvaluationFunction evaluationFunction;
    private final IExpression leftSide;
    private final IExpression rightSide;

    public BinaryOperation(DocContext docContext, EvaluationFunction evaluationFunction, IExpression leftSide, IExpression rightSide)
    {
        super(docContext);
        this.evaluationFunction = evaluationFunction;
        this.leftSide = leftSide;
        this.rightSide = rightSide;
    }

    @Override
    public Object evaluate(IExecutionContext context)
    {
        return evaluationFunction.evaluate(context, leftSide, rightSide);
    }

    @FunctionalInterface
    public interface EvaluationFunction
    {
        Object evaluate(IExecutionContext ctx, IExpression leftSide, IExpression rightSide);
    }
}
