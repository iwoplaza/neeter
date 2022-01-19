package com.neeter.html;

import com.neeter.*;
import com.neeter.document.*;

import java.util.HashMap;
import java.util.Map;

public class HTMLGenerator
{
    private static final Map<StylableProperty, IStringifier<Object>> PROPERTY_PARSER_MAP = new HashMap<>();
    static
    {
        PROPERTY_PARSER_MAP.put(StylableProperty.SIZE, value -> String.format("font-size: %dpx", (int) value));
        PROPERTY_PARSER_MAP.put(StylableProperty.COLOR, value -> String.format("color: #%06X", (int) value));
        PROPERTY_PARSER_MAP.put(StylableProperty.STANDALONE, value -> String.format("display: %s", value.equals(true) ? "block" : "inline"));
        PROPERTY_PARSER_MAP.put(StylableProperty.ALIGNMENT, value -> String.format("text-align: %s", value));
        PROPERTY_PARSER_MAP.put(StylableProperty.WEIGHT, value -> String.format("font-weight: %s", (int) value));
        PROPERTY_PARSER_MAP.put(StylableProperty.MARGIN_TOP, value -> String.format("margin-top: %dpx", (int) value));
        PROPERTY_PARSER_MAP.put(StylableProperty.MARGIN_BOTTOM, value -> String.format("margin-bottom: %dpx", (int) value));
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
            stringBuilder.append(" ");
        }
        else if (node instanceof FormulaNode)
        {
            FormulaNode textNode = ((FormulaNode) node);
            stringBuilder.append("<span class=\"neet-formula\">");
            stringBuilder.append(textNode.getFormula());
            stringBuilder.append("</span>");
        }
        else if (node instanceof NewlineNode)
        {
            stringBuilder.append("</br>");
        }
        else if (node instanceof StyleScope)
        {
            StyleScope scopeNode = (StyleScope) node;

            // <span ...
            StyleClass styleClass = scopeNode.getAppliedClass();
            stringBuilder.append("<span class=\"neet-class-");
            stringBuilder.append(styleClass != null ? styleClass.getKey() : "default");
            stringBuilder.append("\" style=\"");
            // >

            for (Map.Entry<StylableProperty, Object> prop : scopeNode.getProperties())
            {
                stringBuilder.append(PROPERTY_PARSER_MAP.get(prop.getKey()).stringify(prop.getValue()));
                stringBuilder.append("; ");
            }

            stringBuilder.append("\">");

            for (IContentNode childNode : scopeNode.getChildren())
            {
                generateNode(childNode, stringBuilder);
            }

            stringBuilder.append("</span>");

            stringBuilder.append(" ");
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

        stringBuilder.append("body { font-family: sans-serif; }");
        stringBuilder.append(".neet-formula { font-family: serif; display: inline-block; background-color: #ddd; padding: 3px 3px; margin: 0 3px; font-weight: 200; }");

        for (StyleClass styleClass : classRepository.getClasses())
        {
            stringBuilder.append(".neet-class-");
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
