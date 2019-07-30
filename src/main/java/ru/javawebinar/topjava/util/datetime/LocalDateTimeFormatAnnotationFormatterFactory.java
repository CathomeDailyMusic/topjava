package ru.javawebinar.topjava.util.datetime;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public final class LocalDateTimeFormatAnnotationFormatterFactory
        implements AnnotationFormatterFactory<LocalDateTimeFormat> {
    @Override
    public Set<Class<?>> getFieldTypes() {
        return new HashSet<>(Arrays.asList(LocalTime.class, LocalDate.class));
    }

    @Override
    public Printer<?> getPrinter(LocalDateTimeFormat annotation, Class<?> fieldType) {
        return configureFormatterFrom(annotation, fieldType);
    }

    @Override
    public Parser<?> getParser(LocalDateTimeFormat annotation, Class<?> fieldType) {
        return configureFormatterFrom(annotation, fieldType);
    }

    private Formatter<?> configureFormatterFrom(LocalDateTimeFormat annotation, Class<?> fieldType) {
        return fieldType == LocalTime.class ?
                new LocalTimeFormatter(annotation.pattern()) : new LocalDateFormatter(annotation.pattern());
    }

    public final class LocalDateFormatter implements Formatter<LocalDate> {
        private final String pattern;

        public LocalDateFormatter(String pattern) {
            this.pattern = Objects.requireNonNull(pattern);
        }

        @Override
        public LocalDate parse(String text, Locale locale) {
            return text.isBlank() ? null : LocalDate.parse(text, DateTimeFormatter.ofPattern(pattern, locale));
        }

        @Override
        public String print(LocalDate localDate, Locale locale) {
            return localDate == null ? null : localDate.format(DateTimeFormatter.ofPattern(pattern, locale));
        }
    }

    public final class LocalTimeFormatter implements Formatter<LocalTime> {
        private final String pattern;

        public LocalTimeFormatter(String pattern) {
            this.pattern = Objects.requireNonNull(pattern);
        }

        @Override
        public LocalTime parse(String text, Locale locale) {
            return text.isBlank() ? null : LocalTime.parse(text, DateTimeFormatter.ofPattern(pattern, locale));
        }

        @Override
        public String print(LocalTime localTime, Locale locale) {
            return localTime == null ? null : localTime.format(DateTimeFormatter.ofPattern(pattern, locale));
        }
    }
}