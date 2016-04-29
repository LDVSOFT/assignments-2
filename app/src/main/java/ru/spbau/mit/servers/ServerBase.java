package ru.spbau.mit.servers;

import ru.spbau.mit.Protocol.Array;
import ru.spbau.mit.ProtocolUtils;
import ru.spbau.mit.util.TimeCounter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ldvsoft on 29.04.16.
 */
abstract class ServerBase implements Server {
    private final TimeCounter clientTimeCounter = new TimeCounter();
    private final TimeCounter requestTimeCounter = new TimeCounter();

    protected final int onClientStart() {
        return clientTimeCounter.start();
    }

    protected final void onClientEnd(int clientId) {
        clientTimeCounter.end(clientId);
    }

    private int onRequestStart() {
        return requestTimeCounter.start();
    }

    private void onRequestEnd(int requestId) {
        requestTimeCounter.end(requestId);
    }

    protected final Array doHandleRequest(Array request) {
        int requestId = onRequestStart();

        List<Integer> responseList = ProtocolUtils.parseArray(request)
                .stream()
                .sorted()
                .collect(Collectors.toList());
        Array response = ProtocolUtils.buildArray(responseList);

        onRequestEnd(requestId);

        return response;
    }

    @Override
    public final long getAverageClientTime() {
        return clientTimeCounter.getAveradgeTime();
    }

    @Override
    public final long getAverageRequestTime() {
        return requestTimeCounter.getAveradgeTime();
    }
}
