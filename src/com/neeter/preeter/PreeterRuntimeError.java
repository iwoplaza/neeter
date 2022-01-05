package com.neeter.preeter;

import com.neeter.preeter.parse.DocContext;

public class PreeterRuntimeError extends RuntimeException
{
    private DocContext docContext;

    public PreeterRuntimeError(String message, DocContext docContext)
    {
        super(message);
        this.docContext = docContext;
    }

    public PreeterRuntimeError(String message)
    {
        super(message);
        this.docContext = null;
    }

    public DocContext getDocContext()
    {
        return docContext;
    }

    public void setDocContext(DocContext docContext)
    {
        this.docContext = docContext;
    }
}
