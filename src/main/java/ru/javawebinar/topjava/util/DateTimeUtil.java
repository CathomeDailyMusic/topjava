package ru.javawebinar.topjava.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * @param testValue a value to check
     * @param from lower bound to test against, included
     * @param to upper bound to test against, included
     * @param <T> a comparable type
     * @return true, if the test value lies within bounds or equals one of them, false otherwise
     */
    public static <T extends Comparable<T>> boolean isBetween(T testValue, T from, T to) {
        return testValue.compareTo(from) >= 0 && testValue.compareTo(to) <= 0;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}
