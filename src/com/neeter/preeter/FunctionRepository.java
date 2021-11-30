package com.neeter.preeter;

import java.util.HashMap;
import java.util.Map;

public class FunctionRepository
{
    private Map<String, FunctionDefinition> functionDefinitionMap = new HashMap<>();

    public FunctionDefinition getOrMakeFunction(String key)
    {
        return functionDefinitionMap.computeIfAbsent(key, k -> new FunctionDefinition());
    }
}
