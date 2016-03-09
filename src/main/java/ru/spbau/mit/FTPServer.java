package ru.spbau.mit;

import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ldvsoft on 08.03.16.
 */
public class FTPServer implements AutoCloseable {
    private ServerSocket serverSocket;

    public FTPServer(int port) throws IOException {
        synchronized (this) {
            serverSocket = new ServerSocket(port);
        }
        new Thread(this::work).start();
    }

    public synchronized void close() {
        try {
            serverSocket.close();
        } catch (IOException ignored) {
        }
    }

    private Socket accept() throws IOException {
        synchronized (this) {
            if (serverSocket.isClosed()) {
                return null;
            }
        }
        return serverSocket.accept();
    }

    private void work() {
        while (true) {
            try {
                Socket socket = accept();
                if (socket == null) {
                    return;
                }
                new Thread(() -> handleConnection(socket)).start();
            } catch (IOException e) {
                break;
            }
        }
    }

    private void handleConnection(Socket socket) {
        try (FTPConnection connection = new FTPConnection(socket)) {
            boolean isOpen = true;
            while (isOpen) {
                String path;
                int action;
                try {
                    action = connection.readAction();
                } catch (EOFException ignored) {
                    break;
                }
                switch (action) {
                    case FTPConnection.FTP_ACTION_LIST:
                        path = connection.readActionList();
                        doList(path, connection);
                        break;
                    case FTPConnection.FTP_ACTION_GET:
                        path = connection.readActionGet();
                        doGet(path, connection);
                        break;
                    default:
                        System.err.printf("Wrong action from client: %d\n", action);
                        isOpen = false;
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doList(String stringPath, FTPConnection connection) throws IOException {
        Path path = Paths.get(stringPath);
        List<FTPFileEntry> contents;
        try {
            contents = Files
                    .list(path)
                    .map(p -> new FTPFileEntry(p.toString(), Files.isDirectory(p)))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            contents = Collections.emptyList();
        }
        connection.writeList(contents);
    }

    private void doGet(String stringPath, FTPConnection connection) throws IOException {
        Path path = Paths.get(stringPath);
        if (!Files.exists(path)) {
            connection.writeGet(0, null);
            return;
        }
        connection.writeGet(Files.size(path), Files.newInputStream(path));
    }
}
