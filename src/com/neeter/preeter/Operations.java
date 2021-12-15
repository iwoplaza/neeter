package com.neeter.preeter;

import com.neeter.preeter.expression.BinaryOperation;
import com.neeter.util.ValueHelper;

public class Operations
{
    public static final BinaryOperation.EvaluationFunction ADD = (c, a, b) -> {
        Object aValue = a.evaluate(c);
        Object bValue = b.evaluate(c);

        if (aValue instanceof Double || bValue instanceof Double)
            return ValueHelper.asDouble(aValue) + ValueHelper.asDouble(bValue);

        return ValueHelper.requireInt(aValue) + ValueHelper.requireInt(bValue);
    };
    public static final BinaryOperation.EvaluationFunction SUBTRACT = (c, a, b) -> {
        Object aValue = a.evaluate(c);
        Object bValue = b.evaluate(c);

        if (aValue instanceof Double || bValue instanceof Double)
            return ValueHelper.asDouble(aValue) - ValueHelper.asDouble(bValue);

        return ValueHelper.requireInt(aValue) - ValueHelper.requireInt(bValue);
    };
    public static final BinaryOperation.EvaluationFunction MULTIPLY = (c, a, b) -> {
        Object aValue = a.evaluate(c);
        Object bValue = b.evaluate(c);

        if (aValue instanceof Double || bValue instanceof Double)
            return ValueHelper.asDouble(aValue) * ValueHelper.asDouble(bValue);

        return ValueHelper.requireInt(aValue) * ValueHelper.requireInt(bValue);
    };
    public static final BinaryOperation.EvaluationFunction DIVIDE = (c, a, b) -> {
        Object aValue = a.evaluate(c);
        Object bValue = b.evaluate(c);

        if (aValue instanceof Double || bValue instanceof Double)
            return ValueHelper.asDouble(aValue) / ValueHelper.asDouble(bValue);

        return ValueHelper.requireInt(aValue) / ValueHelper.requireInt(bValue);
    };
    public static final BinaryOperation.EvaluationFunction MODULO =     (c, a, b) -> ValueHelper.requireInt(a.evaluate(c)) % ValueHelper.requireInt(b.evaluate(c));
    public static final BinaryOperation.EvaluationFunction EQUALS =     (c, a, b) -> a.evaluate(c).equals(b.evaluate(c));
    public static final BinaryOperation.EvaluationFunction LESS_THAN =  (c, a, b) -> ValueHelper.lessThan(a.evaluate(c), b.evaluate(c));
    public static final BinaryOperation.EvaluationFunction LESS_EQUAL = (c, a, b) -> ValueHelper.lessOrEqual(a.evaluate(c), b.evaluate(c));
    public static final BinaryOperation.EvaluationFunction MORE_THAN =  (c, a, b) -> ValueHelper.moreThan(a.evaluate(c), b.evaluate(c));
    public static final BinaryOperation.EvaluationFunction MORE_EQUAL = (c, a, b) -> ValueHelper.moreOrEqual(a.evaluate(c), b.evaluate(c));
}
