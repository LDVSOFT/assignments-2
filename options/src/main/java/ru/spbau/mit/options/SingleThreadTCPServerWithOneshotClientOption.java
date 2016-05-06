package ru.spbau.mit.options;

import ru.spbau.mit.clients.Client;
import ru.spbau.mit.clients.TCPClient;
import ru.spbau.mit.servers.Server;
import ru.spbau.mit.servers.SingleThreadTCPServer;

import java.io.IOException;

/**
 * Created by ldvsoft on 04.05.16.
 */
@BenchmarkOptionItem
class SingleThreadTCPServerWithOneshotClientOption implements BenchmarkOption {
    @Override
    public String getName() {
        return "TCP: one-shot client connection, everything processed in single process";
    }

    @Override
    public Server getServer() throws IOException {
        return new SingleThreadTCPServer();
    }

    @Override
    public Client getClient() throws IOException {
        return new TCPClient(false);
    }
}
