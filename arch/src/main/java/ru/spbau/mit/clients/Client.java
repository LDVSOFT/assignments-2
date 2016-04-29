package ru.spbau.mit.clients;

/**
 * Created by ldvsoft on 29.04.16.
 */
public interface Client {
    long getAverageTime();

    void run(int requestSize, int requestCount, long delta);
    void join();
}
