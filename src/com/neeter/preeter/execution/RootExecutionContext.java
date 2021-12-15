package com.neeter.preeter.execution;

import com.neeter.preeter.FunctionRepository;
import com.neeter.preeter.PreeterRuntimeError;

import java.util.HashMap;
import java.util.Map;

public class RootExecutionContext extends BaseExecutionContext
{
    private final Map<String, Variable> globalVariables = new HashMap<>();

    public RootExecutionContext(FunctionRepository functionRepository, StringBuilder outputBuilder)
    {
        super(functionRepository, outputBuilder);
    }

    @Override
    public void declareVariable(String key, Object value)
    {
        if (this.globalVariables.containsKey(key))
        {
            throw new PreeterRuntimeError(String.format("Tried to redeclare variable %s in the same scope.", key));
        }
        this.globalVariables.put(key, new Variable(value));
    }

    @Override
    public void setVariable(String key, Object value)
    {
        // This scope's variables take precedence

        if (this.globalVariables.containsKey(key))
        {
            this.globalVariables.get(key).setValue(value);
        }
        else
        {
            throw new PreeterRuntimeError(String.format("Tried to set undeclared variable: %s", key));
        }
    }

    @Override
    public Object getVariable(String key)
    {
        // This scope's variables take precedence

        if (this.globalVariables.containsKey(key))
        {
            return this.globalVariables.get(key).getValue();
        }
        else
        {
            throw new PreeterRuntimeError(String.format("Tried to get undeclared variable: %s", key));
        }
    }

    @Override
    public IExecutionContext createFunctionCall()
    {
        return new ExecutionContext(functionRepository, outputBuilder, this.globalVariables);
    }

    @Override
    public IExecutionContext createDeeperScope()
    {
        return new ExecutionContext(functionRepository, outputBuilder, this.globalVariables);
    }
}
