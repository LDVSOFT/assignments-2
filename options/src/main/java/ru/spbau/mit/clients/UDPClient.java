package ru.spbau.mit.clients;

import ru.spbau.mit.Protocol.Array;
import ru.spbau.mit.util.UDPUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by ldvsoft on 29.04.16.
 */
public class UDPClient extends ClientBase {
    private DatagramSocket socket = new DatagramSocket();

    public UDPClient() throws SocketException {
    }

    @Override
    protected Array doCommunicate(Array request) {
        try {
            DatagramPacket packet = UDPUtils.buildPacket(request);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
