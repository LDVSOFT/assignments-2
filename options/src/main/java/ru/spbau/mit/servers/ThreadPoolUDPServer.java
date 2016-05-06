package ru.spbau.mit.servers;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ldvsoft on 29.04.16.
 */
public final class ThreadPoolUDPServer extends SynchronousUDPServer {
    private final ExecutorService pool = Executors.newCachedThreadPool();

    public ThreadPoolUDPServer() throws IOException {
        super();
    }

    @Override
    protected void submit(Runnable task) {
        pool.submit(task);
    }
}
