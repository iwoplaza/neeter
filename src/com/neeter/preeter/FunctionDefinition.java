package com.neeter.preeter;

import com.neeter.preeter.execution.IExecutionContext;
import com.neeter.preeter.expression.IExpression;

import java.util.ArrayList;
import java.util.List;

public class FunctionDefinition implements ICodeScope, IFunctionDefinition
{
    private boolean defined = false;
    private List<String> parameterNames;
    private final List<IExpression> statements = new ArrayList<>();

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
    public void receiveExpression(IExpression expression)
    {
        statements.add(expression);
    }

    @Override
    public Object evaluate(IExecutionContext context)
    {
        // TODO Return a value

        for (IExpression statement : statements)
        {
            statement.evaluate(context);
        }

        return null;
    }
}
