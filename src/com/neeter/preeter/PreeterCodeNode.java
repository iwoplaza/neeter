package com.neeter.preeter;

import com.neeter.preeter.execution.IExecutionContext;
import com.neeter.preeter.expression.ExpressionBase;
import com.neeter.preeter.expression.IExpression;

import java.util.List;

public class PreeterCodeNode extends ExpressionBase implements IPreeterNode
{
    private final List<IExpression> statements;

    public PreeterCodeNode(int lineIndex, List<IExpression> statements)
    {
        super(lineIndex);
        this.statements = statements;
    }

    @Override
    public Object evaluate(IExecutionContext context)
    {
        for (IExpression statement : statements)
        {
            statement.evaluate(context);
        }

        return null;
    }
}
