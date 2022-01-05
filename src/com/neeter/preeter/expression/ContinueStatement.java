package com.neeter.preeter.expression;

import com.neeter.preeter.execution.BreakException;
import com.neeter.preeter.execution.ContinueException;
import com.neeter.preeter.execution.IExecutionContext;
import com.neeter.preeter.parse.DocContext;

public class ContinueStatement extends ExpressionBase
{
    public ContinueStatement(DocContext docContext)
    {
        super(docContext);
    }

    @Override
    public Object evaluate(IExecutionContext context)
    {
        throw new ContinueException();
    }
}
