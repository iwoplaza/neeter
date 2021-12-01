package com.neeter.preeter;

import com.neeter.preeter.execution.ExecutionContext;
import com.neeter.preeter.execution.IExecutionContext;
import com.neeter.preeter.expression.IExpression;
import com.neeter.preeter.statement.ExpressionEvaluation;
import com.neeter.preeter.statement.IStatement;

import java.util.ArrayList;
import java.util.List;

public class CodeScope implements ICodeScope
{
    private final List<IStatement> statements = new ArrayList<>();

    @Override
    public void addStatement(IStatement statement)
    {
        statements.add(statement);
    }

    @Override
    public void receiveExpression(IExpression expression)
    {
        statements.add(new ExpressionEvaluation(expression));
    }

    @Override
    public Object evaluate(IExecutionContext context)
    {
        IExecutionContext scopeContext = context.createDeeperScope();
        for (IStatement statement : statements)
        {
            statement.run(scopeContext);
        }

        return null;
    }
}
