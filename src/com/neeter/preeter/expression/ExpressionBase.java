package com.neeter.preeter.expression;

import com.neeter.preeter.parse.DocContext;

public abstract class ExpressionBase implements IExpression
{
    protected DocContext docContext;

    public ExpressionBase(DocContext docContext)
    {
        this.docContext = docContext.copy();
    }

    public ExpressionBase(int lineIndex)
    {
        this.docContext = new DocContext(lineIndex, 0);
    }

    @Override
    public DocContext getDocContext()
    {
        return docContext;
    }
}
