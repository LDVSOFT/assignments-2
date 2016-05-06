package ru.spbau.mit.benchmark;

import ru.spbau.mit.clients.Client;
import ru.spbau.mit.options.BenchmarkOption;
import ru.spbau.mit.servers.Server;

import java.io.IOException;
import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Stream;

/**
 * Created by ldvsoft on 04.05.16.
 */
public abstract class BenchmarkRun {
    public static final class Result {
        private String optionName;
        private String changingParamName;
        private final SortedMap<Long, Long> serverClientTimes = new TreeMap<>();
        private final SortedMap<Long, Long> serverRequestTimes = new TreeMap<>();
        private final SortedMap<Long, Long> clientRequestTimes = new TreeMap<>();

        public Result(String optionName, String changingParamName) {
            this.optionName = optionName;
            this.changingParamName = changingParamName;
        }

        public void commitPoint(long x, long serverClientTime, long serverRequestTime, long clientRequestTime) {
            serverClientTimes.put(x, serverClientTime);
            serverRequestTimes.put(x, serverRequestTime);
            clientRequestTimes.put(x, clientRequestTime);
        }

        public String getOptionName() {
            return optionName;
        }

        public String getChangingParamName() {
            return changingParamName;
        }

        public SortedMap<Long, Long> getServerClientTimes() {
            return Collections.unmodifiableSortedMap(serverClientTimes);
        }

        public SortedMap<Long, Long> getServerRequestTimes() {
            return Collections.unmodifiableSortedMap(serverRequestTimes);
        }

        public SortedMap<Long, Long> getClientTimes() {
            return Collections.unmodifiableSortedMap(clientRequestTimes);
        }
    }

    private BenchmarkOption option;
    private Thread workingThread;
    private volatile Result result = null;

    protected BenchmarkRun(BenchmarkOption option) {
        this.option = option;
        workingThread = new Thread(() -> this.result = work());
    }

    public final Result getResult() {
        return result;
    }

    public final void startRun() {
        workingThread.start();
    }

    public final void interrupt() {
        workingThread.interrupt();
    }

    public final void join() {
        try {
            workingThread.join();
        } catch (InterruptedException ignored) {
        }
    }

    protected abstract Result work();

    protected final String getOptionName() {
        return option.getName();
    }

    protected final void measure(
            Result result,
            long param,
            int requestsCount, int clientsCount,
            int requestSize, long requestDelay
    ) {
        try {
            Server server = option.getServer();
            Client[] clients = new Client[clientsCount];
            for (int i = 0; i != clientsCount; i++) {
                clients[i] = option.getClient();
            }
            server.run();
            for (int i = 0; i != clientsCount; i++) {
                clients[i].run(requestSize, requestsCount, requestDelay);
            }

            for (int i = 0; i != clientsCount; i++) {
                clients[i].join();
            }
            server.shutdown();

            long clientAverageTime = Math.round(Stream.of(clients)
                    .mapToLong(Client::getAverageTime)
                    .average()
                    .orElse(0));
            result.commitPoint(
                    param,
                    server.getAverageClientTime(),
                    server.getAverageRequestTime(),
                    clientAverageTime
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
