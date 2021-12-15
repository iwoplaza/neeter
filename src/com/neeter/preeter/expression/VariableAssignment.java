package com.neeter.preeter.expression;

import com.neeter.preeter.execution.IExecutionContext;

public class VariableAssignment implements IExpression
{
    private final String id;
    private final boolean declaration;
    private final IExpression valueExpression;

    public VariableAssignment(String id, boolean declaration, IExpression valueExpression)
    {
        this.id = id;
        this.declaration = declaration;
        this.valueExpression = valueExpression;
    }

    public String getId()
    {
        return id;
    }

    public boolean isDeclaration()
    {
        return declaration;
    }

    public IExpression getValueExpression()
    {
        return valueExpression;
    }

    @Override
    public Object evaluate(IExecutionContext context)
    {
        Object value = null;
        if (valueExpression != null)
        {
            value = valueExpression.evaluate(context);
        }

        if (declaration)
        {
            context.declareVariable(id, value);
        }
        else
        {
            context.setVariable(id, value);
        }

        return value;
    }
}
