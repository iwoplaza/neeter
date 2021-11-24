package com.neeter.html;

import com.neeter.*;

import java.util.HashMap;
import java.util.Map;

public class HTMLGenerator
{
    private static final Map<StylableProperty, IStringifier<Object>> PROPERTY_PARSER_MAP = new HashMap<>();
    static
    {
        PROPERTY_PARSER_MAP.put(StylableProperty.SIZE, value -> String.format("font-size: %dpx", (int) value));
        PROPERTY_PARSER_MAP.put(StylableProperty.COLOR, value -> String.format("color: #%05X", (int) value));
    }

    private final StyleScope rootScope;
    private final ClassRepository classRepository;

    public HTMLGenerator(StyleScope rootScope, ClassRepository classRepository)
    {
        this.rootScope = rootScope;
        this.classRepository = classRepository;
    }

    private void generateNode(IContentNode node, StringBuilder stringBuilder)
    {
        if (node instanceof TextNode)
        {
            TextNode textNode = ((TextNode) node);
            stringBuilder.append(textNode.getText());
        }
        else if (node instanceof FormulaNode)
        {
            FormulaNode textNode = ((FormulaNode) node);
            stringBuilder.append("<span class=\"ntr-formula\">");
            stringBuilder.append(textNode.getFormula());
            stringBuilder.append("</span>");
        }
        else if (node instanceof StyleScope)
        {
            StyleScope scopeNode = (StyleScope) node;

            StyleClass styleClass = scopeNode.getAppliedClass();
            if (styleClass != null)
            {
                stringBuilder.append("<span class=\"ntr-class-");
                stringBuilder.append(styleClass.getKey());
                stringBuilder.append("\">");
            }

            for (IContentNode childNode : scopeNode.getChildren())
            {
                generateNode(childNode, stringBuilder);
            }

            if (styleClass != null)
            {
                stringBuilder.append("</span>");
            }
        }
    }

    public void generateStyleProperty(StylableProperty propKey, Object value, StringBuilder stringBuilder)
    {
        stringBuilder.append(PROPERTY_PARSER_MAP.get(propKey).stringify(value));
        stringBuilder.append(";\n");
    }

    public String generate()
    {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Document</title>\n" +
                "    <style>\n\n");

        for (StyleClass styleClass : classRepository.getClasses())
        {
            stringBuilder.append(".ntr-class-");
            stringBuilder.append(styleClass.getKey());
            stringBuilder.append(" {\n");

            for (Map.Entry<StylableProperty, Object> prop : styleClass.getProperties().entrySet())
            {
                generateStyleProperty(prop.getKey(), prop.getValue(), stringBuilder);
            }

            stringBuilder.append("}\n");
        }

        stringBuilder.append("\n    </style>\n" +
                "</head>\n" +
                "<body>\n");

        generateNode(rootScope, stringBuilder);

        stringBuilder.append("\n" +
                "</body>\n" +
                "</html>");

        return stringBuilder.toString();
    }
}
