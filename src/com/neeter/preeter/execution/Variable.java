package com.neeter.preeter.execution;

public class Variable
{
    private Object value;

    public Variable(Object value)
    {
        this.value = value;
    }

    public Object getValue()
    {
        return value;
    }

    public void setValue(Object value)
    {
        this.value = value;
    }
}
