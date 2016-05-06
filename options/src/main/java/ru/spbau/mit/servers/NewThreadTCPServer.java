package ru.spbau.mit.servers;

import java.io.IOException;

/**
 * Created by ldvsoft on 29.04.16.
 */
public final class NewThreadTCPServer extends SynchronousTCPServer {
    public NewThreadTCPServer() throws IOException {
        super();
    }

    @Override
    protected void submit(Runnable task) {
        new Thread(task).start();
    }
}
