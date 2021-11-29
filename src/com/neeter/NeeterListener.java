package com.neeter;

import com.neeter.grammar.NeeterBaseListener;
import com.neeter.grammar.NeeterParser;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class NeeterListener extends NeeterBaseListener
{
    private final List<String> errors = new ArrayList<>();

    private ClassRepository classRepository = new ClassRepository();
    private final StyleScope rootScope = new StyleScope(null);
    private StyleScope currentScope = rootScope;

    public NeeterListener()
    {
        {
            StyleClass titleClass = new StyleClass("title");
            titleClass.setProperty(StylableProperty.STANDALONE, true);
            titleClass.setProperty(StylableProperty.SIZE, 40);
            titleClass.setProperty(StylableProperty.ALIGNMENT, "center");
            titleClass.setProperty(StylableProperty.MARGIN_BOTTOM, 20);
            titleClass.setProperty(StylableProperty.COLOR, 0x2266aa);
            classRepository.registerClass("title", titleClass);
        }

        {
            StyleClass h1Class = new StyleClass("h1");
            h1Class.setProperty(StylableProperty.STANDALONE, true);
            h1Class.setProperty(StylableProperty.SIZE, 26);
            h1Class.setProperty(StylableProperty.MARGIN_TOP, 10);
            h1Class.setProperty(StylableProperty.MARGIN_BOTTOM, 5);
            h1Class.setProperty(StylableProperty.COLOR, 0x2266aa);
            classRepository.registerClass("h1", h1Class);
        }

        {
            StyleClass strongClass = new StyleClass("strong");
            strongClass.setProperty(StylableProperty.WEIGHT, 600);
            classRepository.registerClass("strong", strongClass);
        }
    }

    public Collection<String> getErrors()
    {
        return errors;
    }

    public StyleScope getRootScope()
    {
        return rootScope;
    }

    public ClassRepository getClassRepository()
    {
        return classRepository;
    }

    @Override
    public void enterStyle_scope(NeeterParser.Style_scopeContext ctx)
    {
        NeeterParser.Style_classContext classContext = ctx.style_class();

        if (classContext != null)
        {
            String classKey = classContext.WORD().getText();
            StyleClass styleClass = classRepository.getStyleClass(classKey);

            if (styleClass == null)
            {
                errors.add(String.format("Missing style class: '%s'", classKey));
            }
            else
            {
                StyleScope scope = new StyleScope(styleClass, this.currentScope);
                this.currentScope.addChildNode(scope);
                this.currentScope = scope;
            }
        }
        else
        {
            StyleScope scope = new StyleScope(null, this.currentScope);
            this.currentScope.addChildNode(scope);
            this.currentScope = scope;

            errors.add("Classless style scope unsupported");
        }
    }

    @Override
    public void exitStyle_scope(NeeterParser.Style_scopeContext ctx)
    {
        this.currentScope = this.currentScope.getParent();
    }

    @Override
    public void enterText(NeeterParser.TextContext ctx)
    {
        List<String> words = ctx.WORD().stream().map(ParseTree::getText).collect(Collectors.toList());
        StringBuilder stringBuilder = new StringBuilder();
        for (String word : words)
        {
            if (stringBuilder.length() != 0)
                stringBuilder.append(' ');
            stringBuilder.append(word);
        }

        this.currentScope.addChildNode(new TextNode(stringBuilder.toString()));
    }

    @Override
    public void enterFormula(NeeterParser.FormulaContext ctx)
    {
        List<String> words = ctx.WORD().stream().map(ParseTree::getText).collect(Collectors.toList());
        StringBuilder stringBuilder = new StringBuilder();
        for (String word : words)
        {
            if (stringBuilder.length() != 0)
                stringBuilder.append(' ');
            stringBuilder.append(word);
        }

        this.currentScope.addChildNode(new FormulaNode(stringBuilder.toString()));
    }

    @Override
    public void enterNewline(NeeterParser.NewlineContext ctx)
    {
        this.currentScope.addChildNode(new NewlineNode());
    }
}
