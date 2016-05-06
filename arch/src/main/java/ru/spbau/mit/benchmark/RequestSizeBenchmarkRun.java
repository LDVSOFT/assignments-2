package ru.spbau.mit.benchmark;

import ru.spbau.mit.options.BenchmarkOption;

/**
 * Created by ldvsoft on 05.05.16.
 */
public final class RequestSizeBenchmarkRun extends BenchmarkRun {
    private int requestsCount;
    private int clientsCount;
    private long requestDelay;

    private int start;
    private int step;
    private int end;

    public RequestSizeBenchmarkRun(
            BenchmarkOption option,
            int requestsCount, int clientsCount, long requestDelay,
            int requestSizeStart, int requestSizeStep, int requestSizeEnd
    ) {
        super(option);
        this.requestsCount = requestsCount;
        this.clientsCount = clientsCount;
        this.requestDelay = requestDelay;
        this.start = requestSizeStart;
        this.step = requestSizeStep;
        this.end = requestSizeEnd;
    }

    @Override
    protected Result work() {
        Result result = new Result(getOptionName(), "Request size");
        for (int requestSize = start; requestSize <= end && !Thread.interrupted(); requestSize += step) {
            measure(result, requestSize, requestsCount, clientsCount, requestSize, requestDelay);
        }
        return result;
    }
}
