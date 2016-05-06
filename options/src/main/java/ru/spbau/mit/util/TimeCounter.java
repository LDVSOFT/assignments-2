package ru.spbau.mit.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by ldvsoft on 29.04.16.
 */
public final class TimeCounter {
    private final AtomicInteger count = new AtomicInteger(0);
    private final AtomicLong sumTime = new AtomicLong(0);
    private final ConcurrentMap<Integer, Long> startTimes = new ConcurrentHashMap<>();

    public int start() {
        long startTime = System.currentTimeMillis();
        int id = count.incrementAndGet();
        startTimes.put(id, startTime);
        return id;
    }

    public void end(int id) {
        long endTime = System.currentTimeMillis();
        long startTime = startTimes.get(id);
        startTimes.remove(id);
        sumTime.addAndGet(endTime - startTime);
    }

    public int getCount() {
        return count.get();
    }

    public long getAveradgeTime() {
        return sumTime.get() / count.get();
    }
}
