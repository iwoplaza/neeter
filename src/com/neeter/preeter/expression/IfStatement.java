package com.neeter.preeter.expression;

import com.neeter.preeter.ICodeScope;
import com.neeter.preeter.PreeterRuntimeError;
import com.neeter.preeter.execution.IExecutionContext;
import com.neeter.util.ValueHelper;

import java.util.ArrayList;
import java.util.List;

public class IfStatement implements ICodeScope
{
    private final IExpression conditionExpression;
    private final List<IExpression> statements = new ArrayList<>();

    public IfStatement(IExpression conditionExpression)
    {
        this.conditionExpression = conditionExpression;
    }

    @Override
    public void receiveExpression(IExpression expression)
    {
        statements.add(expression);
    }

    @Override
    public Object evaluate(IExecutionContext context)
    {
        if (ValueHelper.requireTruthy(conditionExpression.evaluate(context)))
        {
            for (IExpression statement : statements)
            {
                statement.evaluate(context);
            }
        }

        return null;
    }
}
