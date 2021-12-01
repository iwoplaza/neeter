package com.neeter.preeter;

public class PreeterTextNode implements IPreeterNode
{
    private final String text;

    public PreeterTextNode(String text)
    {
        this.text = text;
    }

    public String getText()
    {
        return text;
    }
}
