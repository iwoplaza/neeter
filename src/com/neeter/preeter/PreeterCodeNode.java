package com.neeter.preeter;

import com.neeter.preeter.execution.IExecutionContext;
import com.neeter.preeter.expression.IExpression;

import java.util.ArrayList;
import java.util.List;

public class PreeterCodeNode implements IPreeterNode, ICodeScope
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
        for (IExpression statement : statements)
        {
            statement.evaluate(context);
        }

        return null;
    }
}
