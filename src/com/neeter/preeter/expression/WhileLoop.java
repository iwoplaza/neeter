package com.neeter.preeter.expression;

import com.neeter.preeter.execution.IExecutionContext;
import com.neeter.util.ValueHelper;

import java.util.List;

public class WhileLoop extends ExpressionBase
{
    private final IExpression conditionExpression;
    private final List<IExpression> statements;

    public WhileLoop(int lineIndex, IExpression conditionExpression, List<IExpression> statements)
    {
        super(lineIndex);
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
