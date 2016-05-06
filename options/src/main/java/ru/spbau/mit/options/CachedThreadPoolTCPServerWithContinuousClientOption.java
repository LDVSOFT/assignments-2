package ru.spbau.mit.options;

import ru.spbau.mit.clients.Client;
import ru.spbau.mit.clients.TCPClient;
import ru.spbau.mit.servers.Server;
import ru.spbau.mit.servers.CachedThreadPoolTCPServer;

import java.io.IOException;

/**
 * Created by ldvsoft on 04.05.16.
 */
@BenchmarkOptionItem
class CachedThreadPoolTCPServerWithContinuousClientOption implements BenchmarkOption {
    @Override
    public String getName() {
        return "TCP: continuous client connection, client handled in cached thread pool";
    }

    @Override
    public Server getServer() throws IOException {
        return new CachedThreadPoolTCPServer();
    }

    @Override
    public Client getClient() throws IOException {
        return new TCPClient(true);
    }
}
