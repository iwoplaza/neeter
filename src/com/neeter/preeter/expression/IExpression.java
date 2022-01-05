package com.neeter.preeter.expression;

import com.neeter.preeter.execution.IExecutionContext;
import com.neeter.preeter.parse.DocContext;

public interface IExpression
{
    Object evaluate(IExecutionContext context);
    DocContext getDocContext();
}
