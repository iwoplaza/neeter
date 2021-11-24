package com.neeter;

public enum StylableProperty
{
    COLOR(0xffffff),
    SIZE(16);

    private final Object defaultValue;

    StylableProperty(Object defaultValue)
    {
        this.defaultValue = defaultValue;
    }

    public Object getDefaultValue()
    {
        return defaultValue;
    }
}
