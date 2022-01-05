package com.neeter.preeter.expression;

import com.neeter.preeter.execution.IExecutionContext;

public class LiteralExpression extends ExpressionBase
{
    private final Object value;

    public LiteralExpression(int lineIndex, Object value)
    {
        super(lineIndex);
        this.value = value;
    }

    @Override
    public Object evaluate(IExecutionContext context)
    {
        return value;
    }
}
