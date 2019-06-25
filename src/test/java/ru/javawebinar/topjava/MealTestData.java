package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MealTestData {
    public static final List<Meal> MEALS_USER = Arrays.asList(
            new Meal(100007, LocalDateTime.of(2019, Month.JUNE, 4, 19,0), "Ужин пользователь", 700),
            new Meal(100006, LocalDateTime.of(2019, Month.JUNE, 4, 14,0), "Обед пользователь", 1000),
            new Meal(100005, LocalDateTime.of(2019, Month.JUNE, 4, 10,0), "Завтрак пользователь", 500),
            new Meal(100004, LocalDateTime.of(2019, Month.JUNE, 3, 19,0), "Ужин пользователь", 500),
            new Meal(100003, LocalDateTime.of(2019, Month.JUNE, 3, 14,0), "Обед пользователь", 1000),
            new Meal(100002, LocalDateTime.of(2019, Month.JUNE, 3, 10,0), "Завтрак пользователь", 500)
            );

    public static final List<Meal> MEALS_ADMIN = Arrays.asList(
            new Meal(100013, LocalDateTime.of(2019, Month.JUNE, 2, 19,0), "Ужин админ", 700),
            new Meal(100012, LocalDateTime.of(2019, Month.JUNE, 2, 14,0), "Обед админ", 1100),
            new Meal(100011, LocalDateTime.of(2019, Month.JUNE, 2, 10,0), "Завтрак админ", 300),
            new Meal(100010, LocalDateTime.of(2019, Month.JUNE, 1, 19,0), "Ужин админ", 500),
            new Meal(100009, LocalDateTime.of(2019, Month.JUNE, 1, 14,0), "Обед админ", 1200),
            new Meal(100008, LocalDateTime.of(2019, Month.JUNE, 1, 10,0), "Завтрак админ", 200)
    );

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }
}