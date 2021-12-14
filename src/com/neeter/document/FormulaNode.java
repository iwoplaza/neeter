package com.neeter.document;

public class FormulaNode implements IContentNode
{
    private final String formula;

    public FormulaNode(String formula)
    {
        this.formula = formula;
    }

    public String getFormula()
    {
        return formula;
    }

    @Override
    public ContentType getContentType()
    {
        return ContentType.FORMULA;
    }
}
