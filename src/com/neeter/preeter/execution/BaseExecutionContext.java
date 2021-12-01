package com.neeter.preeter.execution;

import com.neeter.preeter.FunctionRepository;
import com.neeter.preeter.PreeterRuntimeError;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseExecutionContext implements IExecutionContext
{
    protected final FunctionRepository functionRepository;
    protected final StringBuilder outputBuilder;
    protected Collection<Object> args;

    public BaseExecutionContext(FunctionRepository functionRepository, StringBuilder outputBuilder)
    {
        this.functionRepository = functionRepository;
        this.outputBuilder = outputBuilder;
    }

    @Override
    public FunctionRepository getFunctionRepository()
    {
        return functionRepository;
    }

    @Override
    public StringBuilder getOutputBuilder()
    {
        return outputBuilder;
    }

    @Override
    public void setArgs(Collection<Object> args)
    {
        this.args = args;
    }

    @Override
    public Collection<Object> getArgs()
    {
        return args;
    }
}
