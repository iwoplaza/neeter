package com.neeter;

import com.neeter.grammar.NeeterLexer;
import com.neeter.grammar.NeeterParser;
import com.neeter.html.HTMLGenerator;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

public class Main
{
    public static void main(String[] args)
    {
        try
        {
            ProgramArguments programArguments = new ProgramArguments(args);

            // Collecting tokens
            NeeterLexer lexer = new NeeterLexer(programArguments.getCharStream());

            // Parsing
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            NeeterParser parser = new NeeterParser(tokens);
            ParseTree tree = parser.program();

            // Walking the tree
            ParseTreeWalker walker = new ParseTreeWalker();
            NeeterListener listener = new NeeterListener();
            walker.walk(listener, tree);

            Collection<String> error = listener.getErrors();
            if (error.size() > 0)
            {
                System.err.println("Failed to compile, encountered errors:");
                error.forEach(System.err::println);
                return;
            }

            // Generating output
            StyleScope rootScope = listener.getRootScope();
            HTMLGenerator htmlGenerator = new HTMLGenerator(rootScope, listener.getClassRepository());
            String output = htmlGenerator.generate();

            try (BufferedWriter f = new BufferedWriter(new FileWriter(programArguments.getOutputFile("html"))))
            {
                f.write(output);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (IllegalArgumentException e)
        {
            System.err.println("Invalid usage. " + e.getMessage());
        }
    }
}
