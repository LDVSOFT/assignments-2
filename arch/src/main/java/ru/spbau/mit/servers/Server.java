package ru.spbau.mit.servers;

/**
 * Created by ldvsoft on 28.04.16.
 */
public interface Server {
    long getAverageClientTime();
    long getAverageRequestTime();

    void run();
    void shutdown();
}
