package com.neeter.preeter.expression;

import com.neeter.preeter.execution.IExecutionContext;
import com.neeter.preeter.parse.DocContext;

public class IdentifierExpression extends ExpressionBase
{
    private final String identifier;

    public IdentifierExpression(DocContext docContext, String identifier)
    {
        super(docContext);
        this.identifier = identifier;
    }

    @Override
    public Object evaluate(IExecutionContext context)
    {
        return context.getVariable(identifier);
    }
}
