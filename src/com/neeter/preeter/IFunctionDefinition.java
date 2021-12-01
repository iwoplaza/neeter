package com.neeter.preeter;

import com.neeter.preeter.expression.IExpression;

import java.util.ArrayList;
import java.util.List;

@FunctionalInterface
public interface IFunctionDefinition extends IExpression
{
    default boolean isDefined()
    {
        return true;
    }

    default List<String> getNamedParameters()
    {
        return new ArrayList<>();
    }
}
