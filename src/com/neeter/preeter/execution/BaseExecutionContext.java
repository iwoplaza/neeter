package com.neeter.preeter.execution;

import com.neeter.preeter.FunctionRepository;

import java.util.Collection;

public abstract class BaseExecutionContext implements IExecutionContext
{
    protected Collection<Object> args;

    protected final SharedExecutionData sharedExecutionData;

    public BaseExecutionContext(SharedExecutionData sharedExecutionData)
    {
        this.sharedExecutionData = sharedExecutionData;
    }

    @Override
    public FunctionRepository getFunctionRepository()
    {
        return sharedExecutionData.functionRepository;
    }

    @Override
    public StringBuilder getOutputBuilder()
    {
        return sharedExecutionData.outputBuilder;
    }

    @Override
    public int getMaxCallDepth()
    {
        return sharedExecutionData.maxStackDepth;
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
