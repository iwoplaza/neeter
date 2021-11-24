package com.neeter;

@FunctionalInterface
public interface IStringifier<T>
{
    String stringify(T value);
}
