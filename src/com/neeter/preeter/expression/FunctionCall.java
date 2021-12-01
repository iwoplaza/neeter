package com.neeter.preeter.expression;

import com.neeter.preeter.IFunctionDefinition;
import com.neeter.preeter.PreeterRuntimeError;
import com.neeter.preeter.execution.IExecutionContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class FunctionCall implements IExpression, IExpressionHost
{
    private final String id;
    private List<IExpression> argumentExpressions = new ArrayList<>();

    public FunctionCall(String id)
    {
        this.id = id;
    }

    @Override
    public Object evaluate(IExecutionContext context)
    {
        IFunctionDefinition expression = context.getFunctionRepository().getFunction(this.id);

        if (expression == null || !expression.isDefined())
        {
            throw new PreeterRuntimeError(String.format("Tried to call undefined function: %s", this.id));
        }

        Collection<Object> argValues = argumentExpressions.stream().map(e -> e.evaluate(context)).collect(Collectors.toList());

        IExecutionContext functionContext = context.createFunctionCall();
        functionContext.setArgs(argValues);

        return expression.evaluate(functionContext);
    }

    @Override
    public void receiveExpression(IExpression expression)
    {
        argumentExpressions.add(expression);
    }
}
