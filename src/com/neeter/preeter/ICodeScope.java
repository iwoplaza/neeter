package com.neeter.preeter;

import com.neeter.preeter.expression.IExpression;

public interface ICodeScope extends IExpression
{
    void receiveExpression(IExpression expression);
}
