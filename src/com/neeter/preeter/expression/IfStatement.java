package com.neeter.preeter.expression;

import com.neeter.preeter.execution.IExecutionContext;
import com.neeter.util.ValueHelper;

import java.util.List;

public class IfStatement implements IExpression
{
    private final IExpression conditionExpression;
    private final List<IExpression> statements;
    private final List<IExpression> elseExpressions;

    public IfStatement(IExpression conditionExpression, List<IExpression> statements, List<IExpression> elseExpressions)
    {
        this.conditionExpression = conditionExpression;
        this.statements = statements;
        this.elseExpressions = elseExpressions;
    }

    public IfStatement(IExpression conditionExpression, List<IExpression> statements)
    {
        this(conditionExpression, statements, null);
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
        else if (elseExpressions != null)
        {
            for (IExpression statement : elseExpressions)
            {
                statement.evaluate(context);
            }
        }

        return null;
    }
}
