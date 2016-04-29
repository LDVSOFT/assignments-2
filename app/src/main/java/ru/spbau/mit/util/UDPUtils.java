package ru.spbau.mit.util;

import ru.spbau.mit.Protocol.Array;
import ru.spbau.mit.ProtocolUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by ldvsoft on 29.04.16.
 */
public abstract class UDPUtils {
    private static final int MAX_PACKAGE_SIZE = 65507;

    public static DatagramPacket receivePacket(DatagramSocket socket) throws IOException {
        byte[] buffer = new byte[MAX_PACKAGE_SIZE];
        DatagramPacket received = new DatagramPacket(buffer, buffer.length);
        socket.receive(received);
        return received;
    }

    public static DatagramPacket buildPacket(Array array) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ProtocolUtils.writeArrayFully(array, baos);
        byte[] buffer = baos.toByteArray();
        return new DatagramPacket(buffer, buffer.length);
    }

    public static Array parsePacket(DatagramPacket packet) throws IOException {
        return ProtocolUtils.readArrayFully(new ByteArrayInputStream(packet.getData()));
    }
}
