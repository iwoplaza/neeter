package com.neeter;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class ProgramArguments
{
    private final CharStream charStream;
    private final File outputFile;

    public ProgramArguments(String[] args) throws IOException
    {
        if (args.length != 1)
        {
            throw new IllegalArgumentException("Args: <filename>");
        }

        this.charStream = CharStreams.fromFileName(args[0], Charset.defaultCharset());
        this.outputFile = new File(purgeExtension(args[0]));
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

    public CharStream getCharStream()
    {
        return charStream;
    }

    public File getOutputFile(String extension)
    {
        return new File(outputFile.getPath() + "." + extension);
    }
}
