package com.neeter;

public enum StylableProperty
{
    COLOR(0xffffff),
    SIZE(16),
    WEIGHT(200),
    ALIGNMENT("left"),
    MARGIN_BOTTOM(0),
    MARGIN_TOP(0),
    STANDALONE(false);

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
