package ru.javawebinar.topjava.util;

import com.sun.org.apache.xpath.internal.operations.Bool;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import javax.jws.soap.SOAPBinding;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
        List<UserMealWithExceed> filtered =
                getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000);
        if (filtered != null) {
            filtered.forEach(System.out::println);
        }
        List<UserMealWithExceed> filteredStream =
                getFilteredWithExceededStream(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000);
        if (filteredStream != null) {
            filteredStream.forEach(System.out::println);
        }
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        if (mealList == null) {
            return null;
        }

        Map<LocalDate, Integer> dailyCalories   = new HashMap<>();
        List<UserMeal> filteredList             = new ArrayList<>();
        mealList.forEach(meal -> {
            dailyCalories.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum);
            if (TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                filteredList.add(meal);
            }
        });

        if (filteredList.isEmpty()) {
            return null;
        }

        // the result to be returned
        List<UserMealWithExceed> ret = new ArrayList<>();
        for (UserMeal meal : filteredList) {
            ret.add(new UserMealWithExceed(meal, dailyCalories.getOrDefault(meal.getDateTime().toLocalDate(), 0) > caloriesPerDay));
        }
        return ret;
    }

    // optional stream implementation
    public static List<UserMealWithExceed> getFilteredWithExceededStream(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        if (mealList == null) {
            return null;
        }

        Map<LocalDate, Integer> dailyCalories = mealList.stream().collect(Collectors.groupingBy(meal -> meal.getDateTime().toLocalDate(),
                Collectors.summingInt(UserMeal::getCalories)));
        return mealList.stream().filter(meal -> TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> new UserMealWithExceed(meal, dailyCalories.getOrDefault(meal.getDateTime().toLocalDate(), 0) > caloriesPerDay))
                .collect(Collectors.toList());
    }
}
