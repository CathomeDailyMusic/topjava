package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    // Map of users to their meals
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(new Meal(meal.getDateTime(),
                meal.getDescription() + " user 1", meal.getCalories()),
                1));
        MealsUtil.MEALS.forEach(meal -> save(new Meal(meal.getDateTime(),
                        meal.getDescription() + " user 2", meal.getCalories() - 100),
                2));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        Map<Integer, Meal> userMeals = repository.get(userId);
        if (!meal.isNew()) {
            return userMeals == null ? null : userMeals.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
        }
        else {
            meal.setId(counter.incrementAndGet());
            if (userMeals == null) {
                userMeals = new HashMap<>();
                repository.put(userId, userMeals);
            }
            userMeals.put(meal.getId(), meal);
            return meal;
        }
    }

    @Override
    public boolean delete(int id, int userId) {
        Map<Integer, Meal> userMeals = repository.get(userId);
        if (userMeals == null) {
            return false;
        }
        else {
            return userMeals.remove(id) != null;
        }
    }

    @Override
    public Meal get(int id, int userId) {
        Map<Integer, Meal> userMeals = repository.get(userId);
        return userMeals == null ? null : userMeals.get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        Map<Integer, Meal> userMeals = repository.get(userId);
        if (userMeals == null) {
            return new ArrayList<>();
        }
        return userMeals.values().stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getAll(int userId, LocalDate startDate, LocalDate endDate) {
        Map<Integer, Meal> userMeals = repository.get(userId);
        if (userMeals == null) {
            return new ArrayList<>();
        }
        return userMeals.values().stream().filter(meal -> DateTimeUtil.isBetween(meal.getDate(), startDate, endDate))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}