package com.neeter.preeter.execution;

import com.neeter.preeter.FunctionRepository;
import com.neeter.preeter.PreeterRuntimeError;

import java.util.HashMap;
import java.util.Map;

public class ExecutionContext extends BaseExecutionContext
{
    final Map<String, Variable> globalVariables = new HashMap<>();
    final Map<String, Variable> parentVariables = new HashMap<>();
    final Map<String, Variable> variables = new HashMap<>();

    public ExecutionContext(FunctionRepository functionRepository, StringBuilder outputBuilder)
    {
        super(functionRepository, outputBuilder);
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
        ExecutionContext newContext = new ExecutionContext(functionRepository, outputBuilder);

        newContext.args = this.args;
        newContext.globalVariables.putAll(this.globalVariables);

        return newContext;
    }

    @Override
    public IExecutionContext createDeeperScope()
    {
        ExecutionContext newContext = new ExecutionContext(functionRepository, outputBuilder);

        newContext.args = this.args;
        newContext.globalVariables.putAll(this.globalVariables);
        newContext.parentVariables.putAll(this.parentVariables);
        newContext.parentVariables.putAll(this.variables);

        return newContext;
    }
}
