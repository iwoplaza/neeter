package com.neeter.preeter;

import com.neeter.preeter.expression.IExpression;

@FunctionalInterface
public interface IFunctionDefinition extends IExpression
{
    default boolean isDefined()
    {
        return true;
    }
}
