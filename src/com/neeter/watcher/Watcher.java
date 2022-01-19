package com.neeter.watcher;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

public class Watcher implements Runnable
{
    private final File file;
    private final WatchService watchService;
    private final Consumer<String> changeReaction;

    public Watcher(File file, WatchService watchService, Consumer<String> changeReaction)
    {
        this.file = file;
        this.watchService = watchService;
        this.changeReaction = changeReaction;
    }

    @Override
    public void run()
    {
        try
        {
            WatchKey key;

            while ((key = watchService.take()) != null)
            {
                List<WatchEvent<?>> eventList = key.pollEvents();

                for (WatchEvent<?> event : eventList)
                {
                    if (event.kind() == OVERFLOW)
                    {
                        System.out.println("The file event queue overflowed.");
                    }
                    else if (event.kind() == ENTRY_MODIFY)
                    {
                        BufferedReader br = null;
                        boolean properFile;
                        try
                        {
                            br = new BufferedReader(new FileReader(file));
                            properFile = br.readLine() != null;
                        }
                        finally
                        {
                            if (br != null)
                                br.close();
                        }

                        if (properFile)
                        {
                            changeReaction.accept(event.context().toString());
                        }
                    }
                }

                key.reset();
            }
        }
        catch (InterruptedException e)
        {
            System.out.println("Watch service finished execution.");
        }
        catch (IOException e)
        {
            System.out.println("Watch service finished execution.");
            e.printStackTrace();
        }
    }

    public static Watcher watchFile(File file, Consumer<String> changeReaction) throws IOException
    {
        WatchService watcher = FileSystems.getDefault().newWatchService();

        if (file.getParent() == null)
        {
            throw new IllegalArgumentException("Please provide either a proper relative path (./inputFile.neet), or an absolute filepath.");
        }

        Path dir = Paths.get(file.getParent());
        dir.register(watcher, ENTRY_MODIFY);

        return new Watcher(file, watcher, changeReaction);
    }
}
