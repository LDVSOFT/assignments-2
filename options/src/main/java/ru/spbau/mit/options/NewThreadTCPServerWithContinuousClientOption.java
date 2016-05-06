package ru.spbau.mit.options;

import ru.spbau.mit.clients.Client;
import ru.spbau.mit.clients.TCPClient;
import ru.spbau.mit.servers.NewThreadTCPServer;
import ru.spbau.mit.servers.Server;

import java.io.IOException;

/**
 * Created by ldvsoft on 30.04.16.
 */
@BenchmarkOptionItem
class NewThreadTCPServerWithContinuousClientOption implements BenchmarkOption {
    @Override
    public String getName() {
        return "TCP: continuous client connection, client handled in new thread";
    }

    @Override
    public Server getServer() throws IOException {
        return new NewThreadTCPServer();
    }

    @Override
    public Client getClient() throws IOException {
        return new TCPClient(true);
    }
}
