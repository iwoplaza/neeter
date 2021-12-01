package com.neeter.preeter.expression;

import com.neeter.preeter.execution.IExecutionContext;

public class LiteralExpression implements IExpression
{
    private final Object value;

    public LiteralExpression(Object value)
    {
        this.value = value;
    }

    @Override
    public Object evaluate(IExecutionContext context)
    {
        return value;
    }
}
