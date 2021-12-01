package com.neeter.preeter.execution;

import com.neeter.preeter.FunctionRepository;

import java.util.Collection;

public interface IExecutionContext
{
    FunctionRepository getFunctionRepository();
    StringBuilder getOutputBuilder();
    Object getVariable(String key);
    Collection<Object> getArgs();

    void declareVariable(String key, Object value);
    void setVariable(String key, Object value);
    void setArgs(Collection<Object> args);

    IExecutionContext createFunctionCall();
    IExecutionContext createDeeperScope();
}
