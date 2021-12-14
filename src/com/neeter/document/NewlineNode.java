package com.neeter.document;

public class NewlineNode implements IContentNode
{
    @Override
    public ContentType getContentType()
    {
        return ContentType.TEXT;
    }
}
