package com.neeter.preeter.statement;

import com.neeter.preeter.execution.IExecutionContext;

@FunctionalInterface
public interface IStatement
{
    void run(IExecutionContext context);
}
