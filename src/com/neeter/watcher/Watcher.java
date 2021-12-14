package com.neeter.watcher;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

public class Watcher implements Runnable
{
    private final WatchService watchService;
    private final Consumer<String> changeReaction;

    public Watcher(WatchService watchService, Consumer<String> changeReaction)
    {
        this.watchService = watchService;
        this.changeReaction = changeReaction;
    }

    @Override
    public void run()
    {
        try
        {
            WatchKey key = watchService.take();

            while (true)
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
                        changeReaction.accept(event.context().toString());
                    }
                }
            }
        }
        catch (InterruptedException e)
        {
            System.out.println("Watch service finished execution.");
        }
    }

    public static Watcher watchFile(File file, Consumer<String> changeReaction) throws IOException
    {
        WatchService watcher = FileSystems.getDefault().newWatchService();

        Path dir = Paths.get(file.getParent());
        dir.register(watcher, ENTRY_MODIFY);

        return new Watcher(watcher, changeReaction);
    }
}
