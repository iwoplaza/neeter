package com.neeter.preeter;

import com.neeter.preeter.execution.IExecutionContext;
import com.neeter.preeter.expression.IExpression;

import java.util.List;

public class FunctionDefinition implements IExpression, IFunctionDefinition
{
    private boolean defined = false;
    private List<String> parameterNames;
    private List<IExpression> statements = null;

    public FunctionDefinition()
    {
    }

    public FunctionDefinition markDefined(List<String> parameterNames, List<IExpression> statements)
    {
        this.defined = true;
        this.parameterNames = parameterNames;
        this.statements = statements;
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
