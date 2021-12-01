package com.neeter.preeter.expression;

import com.neeter.preeter.execution.IExecutionContext;

import java.util.function.BiFunction;

public class BinaryOperation implements IExpression, IExpressionHost
{
    private final BiFunction<Object, Object, Object> perform;
    private IExpression leftSide;
    private IExpression rightSide;

    public BinaryOperation(BiFunction<Object, Object, Object> perform)
    {
        this.perform = perform;
    }

    @Override
    public Object evaluate(IExecutionContext context)
    {
        assert(leftSide != null);
        assert(rightSide != null);

        return perform.apply(leftSide, rightSide);
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
}
