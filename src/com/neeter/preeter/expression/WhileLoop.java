package com.neeter.preeter.expression;

import com.neeter.preeter.execution.IExecutionContext;
import com.neeter.util.ValueHelper;

import java.util.List;

public class WhileLoop implements IExpression
{
    private final IExpression conditionExpression;
    private final List<IExpression> statements;

    public WhileLoop(IExpression conditionExpression, List<IExpression> statements)
    {
        this.conditionExpression = conditionExpression;
        this.statements = statements;
    }

    @Override
    public Object evaluate(IExecutionContext context)
    {
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
