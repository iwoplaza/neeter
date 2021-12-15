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

    public static int requireInt(Object rawValue)
    {
        if (rawValue instanceof Integer)
        {
            return (int) rawValue;
        }

        throw new PreeterRuntimeError("Value is required to be an integer.");
    }

    public static double asDouble(Object rawValue)
    {
        if (rawValue instanceof Double)
        {
            return (double) rawValue;
        }
        else if (rawValue instanceof Integer)
        {
            return (double) (int) rawValue;
        }

        throw new PreeterRuntimeError(String.format("Couldn't convert value to double: %s", rawValue.toString()));
    }

    public static boolean lessThan(Object a, Object b)
    {
        double aDouble = asDouble(a);
        double bDouble = asDouble(b);

        return aDouble < bDouble;
    }

    public static boolean lessOrEqual(Object a, Object b)
    {
        return lessThan(a, b) || a.equals(b);
    }

    public static boolean moreThan(Object a, Object b)
    {
        double aDouble = asDouble(a);
        double bDouble = asDouble(b);

        return aDouble > bDouble;
    }

    public static boolean moreOrEqual(Object a, Object b)
    {
        return moreThan(a, b) || a.equals(b);
    }
}
