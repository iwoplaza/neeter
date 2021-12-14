package com.neeter;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ProgramArguments
{
    private final File inputFile;
    private final File outputFile;

    private Set<String> flags = new HashSet<>();
    private Map<String, String> arguments = new HashMap<>();

    public ProgramArguments(String[] args) throws IOException
    {
        if (args.length < 1)
        {
            throw new IllegalArgumentException("Args: <filename> (-watch)");
        }

        this.inputFile = new File(args[0]);
        this.outputFile = new File(purgeExtension(args[0]));

        // Parsing flags
        for (String arg : args)
        {
            if (arg.startsWith("-"))
            {
                flags.add(arg.substring(1));
            }
        }
    }

    private static String purgeExtension(String filename)
    {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex == -1)
        {
            return filename;
        }

        return filename.substring(0, dotIndex);
    }

    public File getInputFile()
    {
        return inputFile;
    }

    public File getOutputFile(String extension)
    {
        return new File(outputFile.getPath() + "." + extension);
    }

    public Set<String> getFlags()
    {
        return flags;
    }

    public Map<String, String> getArguments()
    {
        return arguments;
    }
}
