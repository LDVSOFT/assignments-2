package ru.spbau.mit;

import ru.spbau.mit.Protocol.Array;

import java.io.*;
import java.util.List;

/**
 * Created by ldvsoft on 29.04.16.
 */
public abstract class ProtocolUtils {
    public static final int SERVER_PORT = 8082;

    public static Array buildArray(List<Integer> ints) {
        return Array.newBuilder()
                .addAllInt(ints)
                .build();
    }

    public static List<Integer> parseArray(Array array) {
        return array.getIntList();
    }

    public static void writeArrayFully(Array array, OutputStream os) throws IOException {
        byte[] serialized = array.toByteArray();
        DataOutputStream dos = new DataOutputStream(os);
        dos.writeInt(serialized.length);
        dos.write(serialized);
        dos.flush();
        // sic: not closing!
    }

    public static Array readArrayFully(InputStream is) throws IOException {
        DataInputStream dis = new DataInputStream(is);
        int length = dis.readInt();
        byte[] buffer = new byte[length];
        dis.readFully(buffer);
        // sic: not closing!
        return Array.parseFrom(buffer);
    }
}
