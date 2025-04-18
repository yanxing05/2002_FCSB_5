package bto.system.utils;

import java.util.concurrent.atomic.AtomicInteger;

public class IDGenerator {
    private static final AtomicInteger applicationCounter = new AtomicInteger(1000);
    private static final AtomicInteger projectCounter = new AtomicInteger(100);

    public static String generateApplicationID() {
        return "APP-" + applicationCounter.getAndIncrement();
    }

    public static String generateProjectID() {
        return "PRJ-" + projectCounter.getAndIncrement();
    }
}
