package com.neeter.preeter.execution;

public class ReturnException extends RuntimeException
{
    private final Object returnedValue;

    public ReturnException(Object returnedValue)
    {
        this.returnedValue = returnedValue;
    }

    public Object getReturnedValue()
    {
        return returnedValue;
    }
}
