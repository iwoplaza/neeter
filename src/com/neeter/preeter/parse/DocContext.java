package com.neeter.preeter.parse;

import org.antlr.v4.runtime.Token;

public class DocContext
{
    private int line;
    private int column;

    public DocContext(int line, int column)
    {
        this.line = line;
        this.column = column;
    }

    public int getLine()
    {
        return line;
    }

    public int getColumn()
    {
        return column;
    }

    public DocContext copy()
    {
        return new DocContext(this.line, this.column);
    }

    @Override
    public String toString()
    {
        return String.format("%d:%d", line, column);
    }

    public static DocContext fromToken(Token token)
    {
        return new DocContext(token.getLine(), token.getCharPositionInLine());
    }
}
