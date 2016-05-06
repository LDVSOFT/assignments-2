package ru.spbau.mit.options;

import ru.spbau.mit.clients.Client;
import ru.spbau.mit.servers.Server;

import java.io.IOException;

/**
 * Created by ldvsoft on 29.04.16.
 */
public interface BenchmarkOption {
    String getName();
    Server getServer() throws IOException;
    Client getClient() throws IOException;
}
