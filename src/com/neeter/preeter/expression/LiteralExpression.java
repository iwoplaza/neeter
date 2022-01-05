package com.neeter.preeter.expression;

import com.neeter.preeter.execution.IExecutionContext;
import com.neeter.preeter.parse.DocContext;

public class LiteralExpression extends ExpressionBase
{
    private final Object value;

    public LiteralExpression(DocContext docContext, Object value)
    {
        super(docContext);
        this.value = value;
    }

    @Override
    public Object evaluate(IExecutionContext context)
    {
        return value;
    }
}
