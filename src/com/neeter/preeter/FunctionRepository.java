package com.neeter.preeter;

import com.neeter.preeter.expression.IExpression;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionRepository
{
    private Map<String, IFunctionDefinition> functionDefinitionMap = new HashMap<>();

    public void defineBuiltInFunction(String key, IFunctionDefinition def)
    {
        functionDefinitionMap.put(key, def);
    }

    public FunctionDefinition defineFunction(String key, List<String> parameterNames, List<IExpression> statements)
    {
        IExpression prev = functionDefinitionMap.get(key);

        if (prev == null)
        {
            FunctionDefinition def = new FunctionDefinition().markDefined(parameterNames, statements);
            functionDefinitionMap.put(key, def);
            return def;
        }

        if (!(prev instanceof FunctionDefinition))
        {
            throw new PreeterCompileError(String.format("Tried to shadow built-in function: %s", key));
        }

        if (((FunctionDefinition) prev).isDefined())
        {
            throw new PreeterCompileError(String.format("Tried to re-define an existing function: %s", key));
        }

        return ((FunctionDefinition) prev).markDefined(parameterNames, statements);
    }

    public IFunctionDefinition getFunction(String key)
    {
        return functionDefinitionMap.computeIfAbsent(key, k -> new FunctionDefinition());
    }
}
