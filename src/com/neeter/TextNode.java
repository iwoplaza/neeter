package com.neeter;

public class TextNode implements IContentNode
{
    private final String text;

    public TextNode(String text)
    {
        this.text = text;
    }

    public String getText()
    {
        return text;
    }

    @Override
    public ContentType getContentType()
    {
        return ContentType.TEXT;
    }
}
