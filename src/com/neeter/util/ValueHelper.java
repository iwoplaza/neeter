package com.neeter.util;

import com.neeter.preeter.PreeterRuntimeError;

public class ValueHelper
{
    public static boolean requireTruthy(Object rawValue)
    {
        if (rawValue instanceof Boolean)
        {
            return (boolean) rawValue;
        }

        throw new PreeterRuntimeError("Value is required to be a boolean.");
    }
}
