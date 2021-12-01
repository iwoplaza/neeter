package com.neeter.preeter.statement;

import com.neeter.preeter.execution.IExecutionContext;
import com.neeter.preeter.expression.IExpression;
import com.neeter.preeter.expression.IExpressionHost;

public class VariableAssignment implements IStatement, IExpressionHost
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
    public void run(IExecutionContext context)
    {
        // TODO Add variable assignment
    }
}
