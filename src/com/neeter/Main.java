package com.neeter;

import com.neeter.document.NeeterListener;
import com.neeter.document.StyleScope;
import com.neeter.grammar.NeeterLexer;
import com.neeter.grammar.NeeterParser;
import com.neeter.grammar.PreeterLexer;
import com.neeter.grammar.PreeterParser;
import com.neeter.html.HTMLGenerator;
import com.neeter.preeter.FunctionRepository;
import com.neeter.preeter.PreeterCompileError;
import com.neeter.preeter.PreeterEngine;
import com.neeter.preeter.PreeterRuntimeError;
import com.neeter.preeter.parse.DocContext;
import com.neeter.preeter.parse.PreeterVisitor;
import com.neeter.watcher.Watcher;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
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

        // Visiting the tree
        FunctionRepository functionRepository = new FunctionRepository();
        functionRepository.defineBuiltInFunction("show", context -> {
            context.getArgs().forEach(arg -> context.getOutputBuilder().append(arg.toString()));
            return null;
        });
        PreeterVisitor visitor = new PreeterVisitor(functionRepository);

        tree.accept(visitor);

        // Interpreting the language
        PreeterEngine engine = new PreeterEngine(functionRepository);
        return engine.executeNodes(visitor.getNodes());
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

    public static void generateOutput(File inputFile, File outputFile) throws IOException
    {
        CharStream charStream = CharStreams.fromFileName(inputFile.getPath(), Charset.defaultCharset());

        String neeter = "";
        try
        {
            neeter = processPreeter(charStream);
        }
        catch (PreeterRuntimeError e)
        {
            System.err.println("Failed to execute Preeter. Reason: ");
            System.err.println(String.format("[line %s] %s", e.getDocContext(), e.getMessage()));
            return;
        }
        catch (PreeterCompileError error)
        {
            System.err.println("Failed to compile Preeter. Reason: ");
            System.err.println(String.format("[line %s] %s", error.getDocContext(), error.getMessage()));
            return;
        }

        NeeterListener listener = parseNeeter(neeter);
        if (listener == null)
            return;

        // Generating output
        StyleScope rootScope = listener.getRootScope();
        HTMLGenerator htmlGenerator = new HTMLGenerator(rootScope, listener.getClassRepository());
        String output = htmlGenerator.generate();

        try (BufferedWriter f = new BufferedWriter(new FileWriter(outputFile)))
        {
            f.write(output);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
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

        File inputFile = programArguments.getInputFile();
        File outputFile = programArguments.getOutputFile("html");

        try
        {
            generateOutput(inputFile, outputFile);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        if (programArguments.getFlags().contains("watch"))
        {
            // Starting watch...
            System.out.println(String.format("Starting to watch \"%s\"", inputFile.getName()));
            try
            {
                Watcher watcher = Watcher.watchFile(inputFile, (filename) -> {
                    if (inputFile.getName().startsWith(filename))
                    {
                        System.out.println("File changed. Recompiling...");
                        try
                        {
                            generateOutput(inputFile, outputFile);
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
                Thread watchThread = new Thread(watcher);

                watchThread.start();
                watchThread.join();
            }
            catch (IOException | InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
