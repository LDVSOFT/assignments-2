package ru.spbau.mit.servers;

import ru.spbau.mit.Protocol.Array;
import ru.spbau.mit.ProtocolUtils;
import ru.spbau.mit.util.UDPUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by ldvsoft on 29.04.16.
 */
abstract class SynchronousUDPServer extends SynchronousServer {
    private DatagramSocket socket;

    SynchronousUDPServer() throws IOException {
        socket = new DatagramSocket(ProtocolUtils.SERVER_PORT);
    }

    @Override
    public final void shutdown() {
        socket.close();
    }

    @Override
    protected void work() {
        while (true) {
            try {
                DatagramPacket received = UDPUtils.receivePacket(socket);
                int clientId = onClientStart();
                submit(() -> handleRequest(clientId, received));
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private void handleRequest(int clientId, DatagramPacket requestPacket) {
        try {
            Array request = UDPUtils.parsePacket(requestPacket);
            Array response = doHandleRequest(request);
            DatagramPacket responsePacket = UDPUtils.buildPacket(response);
            responsePacket.setSocketAddress(requestPacket.getSocketAddress());
            socket.send(requestPacket);
            onClientEnd(clientId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
