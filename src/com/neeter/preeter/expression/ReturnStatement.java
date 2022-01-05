package com.neeter.preeter.expression;

import com.neeter.preeter.execution.IExecutionContext;
import com.neeter.preeter.execution.ReturnException;
import com.neeter.preeter.parse.DocContext;

public class ReturnStatement extends ExpressionBase
{
    private final IExpression valueExpression;

    public ReturnStatement(DocContext docContext, IExpression valueExpression)
    {
        super(docContext);
        this.valueExpression = valueExpression;
    }

    @Override
    public Object evaluate(IExecutionContext context)
    {
        throw new ReturnException(valueExpression != null ? valueExpression.evaluate(context) : null);
    }
}
