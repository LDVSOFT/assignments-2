package ru.spbau.mit.clients;

import ru.spbau.mit.Protocol.Array;
import ru.spbau.mit.ProtocolUtils;
import ru.spbau.mit.util.TimeCounter;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by ldvsoft on 29.04.16.
 */
abstract class ClientBase implements Client {
    private final Random random = new Random();
    private final TimeCounter timeCounter = new TimeCounter();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private int requestSize;
    private int requestCount;

    @Override
    public synchronized void run(int requestSize, int requestCount, long delay) {
        this.requestSize = requestSize;
        this.requestCount = requestCount;
        scheduler.scheduleAtFixedRate(this::communicate, 0, delay, TimeUnit.MILLISECONDS);
    }

    @Override
    public synchronized void join() {
        while (requestCount > 0) {
            try {
                wait();
            } catch (InterruptedException ignored) {
            }
        }
        scheduler.shutdown();
    }

    @Override
    public final long getAverageTime() {
        return timeCounter.getAveradgeTime();
    }

    protected abstract Array doCommunicate(Array request);

    private void communicate() {
        int requestId;
        Array request;
        synchronized (this) {
            if (requestCount == 0) {
                return;
            }
            requestCount--;
            requestId = timeCounter.start();
            request = prepareRequest();
        }

        // discard response
        doCommunicate(request);
        synchronized (this) {
            timeCounter.end(requestId);
        }
    }

    private Array prepareRequest() {
        List<Integer> requestList = random.ints()
                .limit(requestSize)
                .boxed()
                .collect(Collectors.toList());
        return ProtocolUtils.buildArray(requestList);
    }
}
