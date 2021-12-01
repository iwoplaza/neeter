package com.neeter.preeter;

import com.neeter.preeter.expression.IExpression;
import com.neeter.preeter.expression.IExpressionHost;
import com.neeter.preeter.statement.IStatement;

public interface ICodeScope extends IExpression, IExpressionHost
{
    void addStatement(IStatement statement);
}
