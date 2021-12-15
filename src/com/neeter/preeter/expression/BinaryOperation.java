package com.neeter.preeter.expression;

import com.neeter.preeter.execution.IExecutionContext;

public class BinaryOperation implements IExpression
{
    private final EvaluationFunction evaluationFunction;
    private final IExpression leftSide;
    private final IExpression rightSide;

    public BinaryOperation(EvaluationFunction evaluationFunction, IExpression leftSide, IExpression rightSide)
    {
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
