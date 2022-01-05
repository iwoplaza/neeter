package com.neeter.preeter.expression;

import com.neeter.preeter.execution.BreakException;
import com.neeter.preeter.execution.IExecutionContext;
import com.neeter.preeter.execution.ReturnException;
import com.neeter.preeter.parse.DocContext;

public class BreakStatement extends ExpressionBase
{
    public BreakStatement(DocContext docContext)
    {
        super(docContext);
    }

    @Override
    public Object evaluate(IExecutionContext context)
    {
        throw new BreakException();
    }
}
