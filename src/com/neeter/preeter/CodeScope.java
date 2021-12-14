package com.neeter.preeter;

import com.neeter.preeter.execution.IExecutionContext;
import com.neeter.preeter.expression.IExpression;

import java.util.ArrayList;
import java.util.List;

public class CodeScope implements ICodeScope
{
    private final List<IExpression> statements = new ArrayList<>();

    @Override
    public void receiveExpression(IExpression expression)
    {
        statements.add(expression);
    }

    @Override
    public Object evaluate(IExecutionContext context)
    {
        IExecutionContext scopeContext = context.createDeeperScope();
        for (IExpression statement : statements)
        {
            statement.evaluate(scopeContext);
        }

        return null;
    }
}
