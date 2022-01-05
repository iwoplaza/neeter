package com.neeter.preeter;

import com.neeter.preeter.execution.BreakException;
import com.neeter.preeter.execution.IExecutionContext;
import com.neeter.preeter.execution.ReturnException;
import com.neeter.preeter.expression.ExpressionBase;
import com.neeter.preeter.expression.IExpression;
import com.neeter.preeter.parse.DocContext;

import java.util.List;

public class FunctionDefinition extends ExpressionBase implements IFunctionDefinition
{
    private boolean defined = false;
    private List<String> parameterNames;
    private List<IExpression> statements = null;

    public FunctionDefinition()
    {
        super(0);
    }

    public FunctionDefinition markDefined(DocContext docContext, List<String> parameterNames, List<IExpression> statements)
    {
        this.defined = true;
        this.docContext = docContext;
        this.parameterNames = parameterNames;
        this.statements = statements;
        return this;
    }

    @Override
    public boolean isDefined()
    {
        return defined;
    }

    @Override
    public List<String> getNamedParameters()
    {
        return parameterNames;
    }

    @Override
    public Object evaluate(IExecutionContext context)
    {
        try
        {
            for (IExpression statement : statements)
            {
                statement.evaluate(context);
            }
        }
        catch (ReturnException e)
        {
            return e.getReturnedValue();
        }
        catch (BreakException e)
        {
            // Do nothing
        }

        return null;
    }
}
