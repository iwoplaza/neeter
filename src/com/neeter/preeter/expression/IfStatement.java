package com.neeter.preeter.expression;

import com.neeter.preeter.execution.IExecutionContext;
import com.neeter.preeter.parse.DocContext;
import com.neeter.util.ValueHelper;

import java.util.List;

public class IfStatement extends ExpressionBase
{
    private final IExpression conditionExpression;
    private final List<IExpression> statements;
    private final List<IExpression> elseExpressions;

    public IfStatement(DocContext docContext, IExpression conditionExpression, List<IExpression> statements, List<IExpression> elseExpressions)
    {
        super(docContext);
        this.conditionExpression = conditionExpression;
        this.statements = statements;
        this.elseExpressions = elseExpressions;
    }

    public IfStatement(DocContext docContext, IExpression conditionExpression, List<IExpression> statements)
    {
        this(docContext, conditionExpression, statements, null);
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
