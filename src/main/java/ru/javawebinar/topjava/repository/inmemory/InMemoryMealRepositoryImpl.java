package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepositoryImpl.class);
    // {userId, {MealId, Meal}}
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        final List<Meal> copy1 = new ArrayList<>(MealsUtil.MEALS.size());
        final List<Meal> copy2 = new ArrayList<>(MealsUtil.MEALS.size());
        for (Meal meal : MealsUtil.MEALS) {
            Meal mealCopy = new Meal(null, meal.getDateTime(),
                    meal.getDescription(), meal.getCalories());
            copy1.add(mealCopy);
            mealCopy = new Meal(null, meal.getDateTime(),
                    meal.getDescription(), meal.getCalories());
            copy2.add(mealCopy);
        }
        copy1.forEach(meal -> save(meal, 1));
        copy2.forEach(meal -> save(meal, 2));
        delete(counter.get(), 2);
    }

    @Override
    public Meal save(Meal meal, int userId) {
        // log.info("save {} for user = {}", meal, userId);
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
        // log.info("delete {} of user = {}", id, userId);
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
        // log.info("get {} of user = {}", id, userId);
        Map<Integer, Meal> userMeals = repository.get(userId);
        return userMeals == null ? null : userMeals.get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        // log.info("get all of user = {}", userId);
        Map<Integer, Meal> userMeals = repository.get(userId);
        if (userMeals == null) {
            return new ArrayList<>();
        }
        return userMeals.values().stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        InMemoryMealRepositoryImpl impl = new InMemoryMealRepositoryImpl();
        System.out.println("user 1");
        Collection<Meal> meals1 = impl.getAll(1);
        meals1.forEach(System.out::println);
        System.out.println("user 2");
        Collection<Meal> meals2 = impl.getAll(2);
        meals2.forEach(System.out::println);
        System.out.println("end");
    }
}