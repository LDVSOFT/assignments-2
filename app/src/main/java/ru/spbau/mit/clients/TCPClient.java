package ru.spbau.mit.clients;

import ru.spbau.mit.Protocol.Array;
import ru.spbau.mit.ProtocolUtils;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by ldvsoft on 29.04.16.
 */
public class TCPClient extends ClientBase {
    private boolean isContinuous;
    private Socket socket;

    public TCPClient(boolean isContinuous) {
        this.isContinuous = isContinuous;
    }

    @Override
    public synchronized void join() {
        if (isContinuous) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.join();
    }

    @Override
    protected Array doCommunicate(Array request) {
        try {
            if (socket == null) {
                socket = new Socket("localhost", ProtocolUtils.SERVER_PORT);
            }
            ProtocolUtils.writeArrayFully(request, socket.getOutputStream());
            Array response = ProtocolUtils.readArrayFully(socket.getInputStream());
            if (!isContinuous) {
                socket.close();
                socket = null;
            }
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            try {
                socket.close();
            } catch (IOException e1) {
                e.addSuppressed(e1);
            }
            socket = null;
            return null;
        }
    }
}
