package com.neeter;

import java.util.HashMap;
import java.util.Map;

public class ClassRepository
{
    private Map<String, StyleClass> classes = new HashMap<>();

    public void registerClass(String key, StyleClass styleClass)
    {
        classes.put(key, styleClass);
    }

    public StyleClass getStyleClass(String key)
    {
        return classes.get(key);
    }

    public Iterable<StyleClass> getClasses()
    {
        return classes.values();
    }
}
