package com.neeter;

import com.neeter.grammar.NeeterLexer;
import com.neeter.grammar.NeeterParser;
import com.neeter.grammar.PreeterLexer;
import com.neeter.grammar.PreeterParser;
import com.neeter.html.HTMLGenerator;
import com.neeter.preeter.FunctionRepository;
import com.neeter.preeter.PreeterEngine;
import com.neeter.preeter.PreeterListener;
import com.neeter.preeter.PreeterRuntimeError;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

public class Main
{
    private static String processPreeter(CharStream charStream)
    {
        // Collecting tokens
        PreeterLexer lexer = new PreeterLexer(charStream);

        // Parsing
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PreeterParser parser = new PreeterParser(tokens);
        ParseTree tree = parser.program();

        // Walking the tree
        ParseTreeWalker walker = new ParseTreeWalker();
        FunctionRepository functionRepository = new FunctionRepository();
        functionRepository.defineBuiltInFunction("show", context -> {
            context.getArgs().forEach(arg -> context.getOutputBuilder().append(arg.toString()));
            return null;
        });
        PreeterListener listener = new PreeterListener(functionRepository);

        walker.walk(listener, tree);

        // Interpreting the language
        PreeterEngine engine = new PreeterEngine(functionRepository);
        return engine.executeNodes(listener.getNodes());
    }

    private static NeeterListener parseNeeter(String neeterCode)
    {
        // Collecting tokens
        NeeterLexer lexer = new NeeterLexer(CharStreams.fromString(neeterCode));

        // Parsing
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        NeeterParser parser = new NeeterParser(tokens);
        ParseTree tree = parser.program();

        // Walking the tree
        ParseTreeWalker walker = new ParseTreeWalker();
        NeeterListener listener = new NeeterListener();

        try
        {
            walker.walk(listener, tree);
        }
        catch (Exception e)
        {
            if (listener.getErrors().size() == 0)
            {
                e.printStackTrace();
                return null;
            }
        }

        Collection<String> errors = listener.getErrors();
        if (errors.size() > 0)
        {
            System.err.println("Failed to compile, encountered errors:");
            errors.forEach(System.err::println);
            return null;
        }

        return listener;
    }

    public static void main(String[] args)
    {
        ProgramArguments programArguments;
        try
        {
            programArguments = new ProgramArguments(args);
        }
        catch (IllegalArgumentException | IOException e)
        {
            System.err.println("Invalid usage. " + e.getMessage());
            return;
        }

        String neeter = "";
        try
        {
            neeter = processPreeter(programArguments.getCharStream());
        }
        catch (PreeterRuntimeError e)
        {
            System.err.println("Failed to execute Preeter. Reason: ");
            System.err.println("-- " + e.getMessage());
            return;
        }

        NeeterListener listener = parseNeeter(neeter);
        if (listener == null)
            return;

        // Generating output
        StyleScope rootScope = listener.getRootScope();
        HTMLGenerator htmlGenerator = new HTMLGenerator(rootScope, listener.getClassRepository());
        String output = htmlGenerator.generate();

        try (BufferedWriter f = new BufferedWriter(new FileWriter(programArguments.getOutputFile("html"))))
        {
            f.write(output);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
