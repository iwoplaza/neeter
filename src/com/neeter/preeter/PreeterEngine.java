package com.neeter.preeter;

import com.neeter.preeter.execution.RootExecutionContext;

import java.util.List;

public class PreeterEngine
{
    private final FunctionRepository functionRepository;

    public PreeterEngine(FunctionRepository functionRepository)
    {
        this.functionRepository = functionRepository;
    }

    public String executeNodes(List<IPreeterNode> nodes)
    {
        StringBuilder stringBuilder = new StringBuilder();

        RootExecutionContext context = new RootExecutionContext(functionRepository, stringBuilder);

        for (IPreeterNode node : nodes)
        {
            if (node instanceof PreeterTextNode)
            {
                stringBuilder.append(((PreeterTextNode) node).getText());
            }
            else if (node instanceof PreeterCodeNode)
            {
                PreeterCodeNode codeNode = (PreeterCodeNode) node;

                codeNode.evaluate(context);
            }
        }

        return stringBuilder.toString();
    }
}
