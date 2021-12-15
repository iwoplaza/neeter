package com.neeter.preeter.expression;

import com.neeter.preeter.ICodeScope;
import com.neeter.preeter.PreeterRuntimeError;
import com.neeter.preeter.execution.IExecutionContext;
import com.neeter.util.ValueHelper;

import java.util.ArrayList;
import java.util.List;

public class WhileLoop implements ICodeScope
{
    private IExpression conditionExpression = null;
    private final List<IExpression> statements = new ArrayList<>();

    public WhileLoop()
    {
    }

    @Override
    public void receiveExpression(IExpression expression)
    {
        if (conditionExpression == null)
        {
            conditionExpression = expression;
        }
        else
        {
            statements.add(expression);
        }
    }

    @Override
    public Object evaluate(IExecutionContext context)
    {
        if (conditionExpression == null)
        {
            throw new PreeterRuntimeError("Found while loop without condition expression.");
        }

        while (ValueHelper.requireTruthy(conditionExpression.evaluate(context)))
        {
            for (IExpression statement : statements)
            {
                statement.evaluate(context);
            }
        }

        return null;
    }
}
