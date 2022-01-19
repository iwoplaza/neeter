package com.neeter.document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StyleScope implements IContentNode
{
    private final StyleScope parent;
    private final StyleClass appliedClass;
    private final Map<StylableProperty, Object> properties = new HashMap<>();
    private final List<IContentNode> children = new ArrayList<>();

    public StyleScope(StyleClass appliedClass)
    {
        this(appliedClass, null);
    }

    public StyleScope(StyleClass appliedClass, StyleScope parent)
    {
        this.appliedClass = appliedClass;
        this.parent = parent;
    }

    public StyleScope getParent()
    {
        return parent;
    }

    public void addChildNode(IContentNode node)
    {
        this.children.add(node);
    }

    public StyleClass getAppliedClass()
    {
        return appliedClass;
    }

    public Iterable<Map.Entry<StylableProperty, Object>> getProperties()
    {
        return properties.entrySet();
    }

    public Iterable<IContentNode> getChildren()
    {
        return children;
    }

    @Override
    public ContentType getContentType()
    {
        return ContentType.SCOPE;
    }

    public void setProperty(StylableProperty propKey, Object value)
    {
        this.properties.put(propKey, value);
    }
}
