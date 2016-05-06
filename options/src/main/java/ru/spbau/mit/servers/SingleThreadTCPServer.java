package ru.spbau.mit.servers;

import java.io.IOException;

/**
 * Created by ldvsoft on 04.05.16.
 */
public final class SingleThreadTCPServer extends SynchronousTCPServer {
    private boolean wasFirstRun = false;

    public SingleThreadTCPServer() throws IOException {
    }

    @Override
    protected void submit(Runnable task) {
        if (wasFirstRun) {
            task.run();
        } else {
            // First task is actually accept cycle
            new Thread(task).start();
        }
    }
}
