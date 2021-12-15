package com.neeter.preeter.execution;

import com.neeter.preeter.FunctionRepository;
import com.neeter.preeter.PreeterRuntimeError;

import java.util.HashMap;
import java.util.Map;

public class ExecutionContext extends BaseExecutionContext
{
    final Map<String, Variable> globalVariables;
    final Map<String, Variable> parentVariables = new HashMap<>();
    final Map<String, Variable> variables = new HashMap<>();

    public ExecutionContext(FunctionRepository functionRepository, StringBuilder outputBuilder, Map<String, Variable> globalVariables)
    {
        super(functionRepository, outputBuilder);
        this.globalVariables = globalVariables;
    }

    public ExecutionContext(FunctionRepository functionRepository, StringBuilder outputBuilder)
    {
        this(functionRepository, outputBuilder, new HashMap<>());
    }

    @Override
    public void declareVariable(String key, Object value)
    {
        if (this.variables.containsKey(key))
        {
            throw new PreeterRuntimeError(String.format("Tried to redeclare variable %s in the same scope.", key));
        }
        this.variables.put(key, new Variable(value));
    }

    @Override
    public void setVariable(String key, Object value)
    {
        // This scope's variables take precedence

        if (this.variables.containsKey(key))
        {
            this.variables.get(key).setValue(value);
        }
        else if (this.parentVariables.containsKey(key))
        {
            this.parentVariables.get(key).setValue(value);
        }
        else if (this.globalVariables.containsKey(key))
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

        if (this.variables.containsKey(key))
        {
            return this.variables.get(key).getValue();
        }
        else if (this.parentVariables.containsKey(key))
        {
            return this.parentVariables.get(key).getValue();
        }
        else if (this.globalVariables.containsKey(key))
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
        ExecutionContext newContext = new ExecutionContext(functionRepository, outputBuilder, this.globalVariables);

        newContext.args = this.args;
        newContext.parentVariables.putAll(this.parentVariables);
        newContext.parentVariables.putAll(this.variables);

        return newContext;
    }
}
