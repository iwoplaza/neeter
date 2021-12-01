package com.neeter.preeter.expression;

import com.neeter.preeter.execution.IExecutionContext;

public class IdentifierExpression implements IExpression
{
    private final String identifier;

    public IdentifierExpression(String identifier)
    {
        this.identifier = identifier;
    }

    @Override
    public Object evaluate(IExecutionContext context)
    {
        return context.getVariable(identifier);
    }
}
