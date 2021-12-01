package com.neeter.preeter;

import com.neeter.preeter.execution.IExecutionContext;
import com.neeter.preeter.expression.IExpression;
import com.neeter.preeter.statement.ExpressionEvaluation;
import com.neeter.preeter.statement.IStatement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FunctionDefinition implements ICodeScope, IFunctionDefinition
{
    private boolean defined = false;
    private List<String> parameterNames;
    private final List<IStatement> statements = new ArrayList<>();

    public FunctionDefinition()
    {
    }

    public FunctionDefinition markDefined(List<String> parameterNames)
    {
        this.defined = true;
        this.parameterNames = parameterNames;
        return this;
    }

    @Override
    public boolean isDefined()
    {
        return defined;
    }

    @Override
    public List<String> getNamedParameters()
    {
        return parameterNames;
    }

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
        // TODO Return a value

        for (IStatement statement : statements)
        {
            statement.run(context);
        }

        return null;
    }
}
