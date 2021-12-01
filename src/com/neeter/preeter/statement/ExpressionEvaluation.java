package com.neeter.preeter.statement;

import com.neeter.preeter.execution.IExecutionContext;
import com.neeter.preeter.expression.IExpression;

public class ExpressionEvaluation implements IStatement
{
    private final IExpression expression;

    public ExpressionEvaluation(IExpression expression)
    {
        this.expression = expression;
    }

    @Override
    public void run(IExecutionContext context)
    {
        this.expression.evaluate(context);
    }
}
