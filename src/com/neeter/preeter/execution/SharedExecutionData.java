package com.neeter.preeter.execution;

import com.neeter.preeter.FunctionRepository;

public class SharedExecutionData
{
    public final FunctionRepository functionRepository;
    public final StringBuilder outputBuilder;
    public final int maxStackDepth;

    public SharedExecutionData(FunctionRepository functionRepository, StringBuilder outputBuilder, int maxStackDepth)
    {
        this.functionRepository = functionRepository;
        this.outputBuilder = outputBuilder;
        this.maxStackDepth = maxStackDepth;
    }
}
