package com.neeter.preeter.expression;

import com.neeter.preeter.execution.IExecutionContext;

@FunctionalInterface
public interface IExpression
{
    Object evaluate(IExecutionContext context);
}
