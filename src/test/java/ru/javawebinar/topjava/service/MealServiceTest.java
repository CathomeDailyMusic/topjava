package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(USER_MEAL_6.getId(), USER_ID);
        assertMatch(meal, USER_MEAL_6);
    }

    @Test(expected = NotFoundException.class)
    public void getAnother() {
        service.get(USER_MEAL_6.getId(), ADMIN_ID);
    }

    @Test
    public void delete() {
        service.delete(USER_MEAL_6.getId(), USER_ID);
        assertMatch(service.getAll(USER_ID), USER_MEAL_5, USER_MEAL_4, USER_MEAL_3, USER_MEAL_2, USER_MEAL_1);
    }

    @Test(expected = NotFoundException.class)
    public void deleteAnother() {
        service.delete(USER_MEAL_6.getId(), ADMIN_ID);
    }

    @Test
    public void getBetweenDates() {
        Iterable<Meal> meals = service.getBetweenDates(LocalDate.of(2019, 6, 3), LocalDate.of(2019, 6, 3), USER_ID);
        assertMatch(meals, USER_MEAL_3, USER_MEAL_2, USER_MEAL_1);
    }

    @Test
    public void getBetweenDateTimes() {
        Iterable<Meal> meals = service.getBetweenDateTimes(LocalDateTime.of(2019, 6, 3, 9, 0), LocalDateTime.of(2019, 6, 3, 15, 0), USER_ID);
        assertMatch(meals, USER_MEAL_2, USER_MEAL_1);
    }

    @Test
    public void getAll() {
        Iterable<Meal> meals = service.getAll(USER_ID);
        assertMatch(meals, MEALS_USER);
    }

    @Test
    public void update() {
        Meal updated = service.get(USER_MEAL_6.getId(), USER_ID);
        updated.setDescription("test update");
        service.update(updated, USER_ID);
        assertMatch(service.get(updated.getId(), USER_ID), updated);
    }

    @Test(expected = NotFoundException.class)
    public void updateAnother() {
        Meal updated = service.get(USER_MEAL_6.getId(), USER_ID);
        updated.setDescription("update another");
        service.update(updated, ADMIN_ID);
    }

    @Test
    public void create() {
        Meal newMeal = new Meal(LocalDateTime.now(), "test", 400);
        Meal created = service.create(newMeal, USER_ID);
        newMeal.setId(created.getId());
        assertMatch(created, newMeal);
    }

    @Test(expected = DataAccessException.class)
    public void createDuplicateDateTime() {
        Meal meal       = service.get(USER_MEAL_6.getId(), USER_ID);
        Meal duplicate  = new Meal(meal);
        duplicate.setDescription("duplicate dateTime");
        duplicate.setCalories(0);
        service.create(duplicate, USER_ID);
    }
}