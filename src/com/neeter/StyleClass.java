package com.neeter;

import java.util.HashMap;
import java.util.Map;

public class StyleClass
{
    private final String key;
    private final Map<StylableProperty, Object> properties = new HashMap<>();

    public StyleClass(String key)
    {
        this.key = key;
    }

    public void setProperty(StylableProperty prop, Object value)
    {
        this.properties.put(prop, value);
    }

    public Map<StylableProperty, Object> getProperties()
    {
        return properties;
    }

    public String getKey()
    {
        return key;
    }
}
