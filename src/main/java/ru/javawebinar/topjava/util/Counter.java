package ru.javawebinar.topjava.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * https://www.baeldung.com/java-atomic-variables
 */
public class Counter {
    private final AtomicLong counter = new AtomicLong(0);

    private long getValue() {
        return counter.get();
    }

    private void increment() {
        while (true) {
            long currentValue = getValue();
            long newValue = currentValue + 1;
            if (counter.compareAndSet(currentValue, newValue)) {
                return;
            }
        }
    }

    public long incrementAndGet() {
        increment();
        return getValue();
    }
}
