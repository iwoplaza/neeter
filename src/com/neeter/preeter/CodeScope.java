package com.neeter.preeter;

import com.neeter.preeter.execution.IExecutionContext;
import com.neeter.preeter.expression.IExpression;

import java.util.List;

public class CodeScope implements IExpression
{
    private final List<IExpression> statements;

    public CodeScope(List<IExpression> statements)
    {
        this.statements = statements;
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
