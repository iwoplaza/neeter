package com.neeter.preeter.expression;

import com.neeter.preeter.ICodeScope;
import com.neeter.preeter.execution.IExecutionContext;

public class VariableAssignment implements ICodeScope
{
    private final String id;
    private final boolean declaration;
    private IExpression valueExpression = null;

    public VariableAssignment(String id, boolean declaration)
    {
        this.id = id;
        this.declaration = declaration;
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
    public void receiveExpression(IExpression expression)
    {
        valueExpression = expression;
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
