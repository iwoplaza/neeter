package com.neeter.preeter.expression;

import com.neeter.preeter.ICodeScope;
import com.neeter.preeter.IFunctionDefinition;
import com.neeter.preeter.PreeterRuntimeError;
import com.neeter.preeter.execution.IExecutionContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FunctionCall implements ICodeScope
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

        // General argument values
        List<Object> argValues = argumentExpressions.stream().map(e -> e.evaluate(context)).collect(Collectors.toList());

        IExecutionContext functionContext = context.createFunctionCall();
        functionContext.setArgs(argValues);

        // Named parameters
        List<String> parameterNames = expression.getNamedParameters();

        if (parameterNames.size() > argValues.size())
        {
            throw new PreeterRuntimeError(String.format("Function %s only takes %d arguments, %d given", this.id, parameterNames.size(), argValues.size()));
        }

        for (int i = 0; i < parameterNames.size(); ++i)
        {
            String param = parameterNames.get(i);
            Object value = argValues.get(i);

            functionContext.declareVariable(param, value);
        }

        return expression.evaluate(functionContext);
    }

    @Override
    public void receiveExpression(IExpression expression)
    {
        argumentExpressions.add(expression);
    }
}