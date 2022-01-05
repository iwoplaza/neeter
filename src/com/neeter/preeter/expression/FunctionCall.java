package com.neeter.preeter.expression;

import com.neeter.preeter.IFunctionDefinition;
import com.neeter.preeter.PreeterRuntimeError;
import com.neeter.preeter.execution.IExecutionContext;
import com.neeter.preeter.parse.DocContext;

import java.util.List;
import java.util.stream.Collectors;

public class FunctionCall extends ExpressionBase
{
    private final String id;
    private List<IExpression> argumentExpressions;

    public FunctionCall(DocContext docContext, String id, List<IExpression> argumentExpressions)
    {
        super(docContext);
        this.id = id;
        this.argumentExpressions = argumentExpressions;
    }

    @Override
    public Object evaluate(IExecutionContext context)
    {
        IFunctionDefinition expression = context.getFunctionRepository().getFunction(this.id);

        if (expression == null || !expression.isDefined())
        {
            throw new PreeterRuntimeError(String.format("Tried to call undefined function: %s", this.id), docContext);
        }

        // General argument values
        List<Object> argValues = argumentExpressions.stream().map(e -> e.evaluate(context)).collect(Collectors.toList());

        IExecutionContext functionContext = context.createFunctionCall();
        functionContext.setArgs(argValues);

        // Named parameters
        List<String> parameterNames = expression.getNamedParameters();

        if (parameterNames.size() > argValues.size())
        {
            throw new PreeterRuntimeError(String.format("Function %s only takes %d arguments, %d given", this.id, parameterNames.size(), argValues.size()), docContext);
        }

        for (int i = 0; i < parameterNames.size(); ++i)
        {
            String param = parameterNames.get(i);
            Object value = argValues.get(i);

            functionContext.declareVariable(param, value);
        }

        return expression.evaluate(functionContext);
    }
}
