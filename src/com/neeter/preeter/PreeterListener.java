package com.neeter.preeter;

import com.neeter.grammar.PreeterParser;
import com.neeter.grammar.PreeterParserBaseListener;

public class PreeterListener extends PreeterParserBaseListener
{
    private final FunctionRepository functionRepository;

    private ICodeScope currentScope = null;

    public PreeterListener(FunctionRepository functionRepository)
    {
        this.functionRepository = functionRepository;
    }

    @Override
    public void enterFuncDef(PreeterParser.FuncDefContext ctx)
    {
        super.enterFuncDef(ctx);

        String funcId = ctx.ID().getText();
        FunctionDefinition funcDef = functionRepository.getOrMakeFunction(funcId);

        currentScope = funcDef;
    }

    @Override
    public void enterVariableDecl(PreeterParser.VariableDeclContext ctx)
    {
        super.enterVariableDecl(ctx);


    }
}
