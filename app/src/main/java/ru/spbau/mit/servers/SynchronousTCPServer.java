package ru.spbau.mit.servers;

import ru.spbau.mit.Protocol.Array;
import ru.spbau.mit.ProtocolUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by ldvsoft on 29.04.16.
 */
abstract class SynchronousTCPServer extends SynchronousServer {
    private ServerSocket serverSocket;

    SynchronousTCPServer() throws IOException {
        serverSocket = new ServerSocket(ProtocolUtils.SERVER_PORT);
    }

    @Override
    public final void shutdown() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void work() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                submit(() -> handleRequest(socket));
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private void handleRequest(Socket socket1) {
        try (Socket socket = socket1) {
            while (true) {
                Array request = ProtocolUtils.readArrayFully(socket.getInputStream());
                int clientId = onClientStart();
                Array response = doHandleRequest(request);
                ProtocolUtils.writeArrayFully(response, socket.getOutputStream());
                onClientEnd(clientId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
