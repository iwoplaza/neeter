package com.neeter.preeter.expression;

import com.neeter.preeter.ICodeScope;
import com.neeter.preeter.execution.IExecutionContext;

public class BinaryOperation implements ICodeScope
{
    private final EvaluationFunction evaluationFunction;
    private IExpression leftSide;
    private IExpression rightSide;

    public BinaryOperation(EvaluationFunction evaluationFunction)
    {
        this.evaluationFunction = evaluationFunction;
    }

    @Override
    public Object evaluate(IExecutionContext context)
    {
        assert(leftSide != null);
        assert(rightSide != null);

        return evaluationFunction.evaluate(context, leftSide, rightSide);
    }

    @Override
    public void receiveExpression(IExpression expression)
    {
        if (leftSide == null)
        {
            leftSide = expression;
        }
        else if (rightSide == null)
        {
            rightSide = expression;
        }
        else
        {
            throw new IllegalStateException("A binary operation can only take exactly two child expressions.");
        }
    }

    @FunctionalInterface
    public interface EvaluationFunction
    {
        Object evaluate(IExecutionContext ctx, IExpression leftSide, IExpression rightSide);
    }
}
