package com.neeter.preeter;

import com.neeter.preeter.parse.DocContext;

public class PreeterCompileError extends RuntimeException
{
    private DocContext docContext;

    public PreeterCompileError(String message, DocContext docContext)
    {
        super(message);
        this.docContext = docContext;
    }

    public DocContext getDocContext()
    {
        return docContext;
    }
}
