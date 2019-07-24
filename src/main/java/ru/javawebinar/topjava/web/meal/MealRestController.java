package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
public class MealRestController extends AbstractMealController {
    public Meal get(int id) {
        return getInternal(id);
    }

    public List<MealTo> getAll() {
        return getAllInternal();
    }

    /**
     * <ol>Filter separately
     * <li>by date</li>
     * <li>by time for every date</li>
     * </ol>
     */
    public List<MealTo> getBetween(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        return getBetweenInternal(startDate, startTime, endDate, endTime);
    }

    public Meal create(Meal meal) {
        return createInternal(meal);
    }

    public void update(Meal meal, int id) {
        updateInternal(meal, id);
    }

    public void delete(int id) {
        deleteInternal(id);
    }
}