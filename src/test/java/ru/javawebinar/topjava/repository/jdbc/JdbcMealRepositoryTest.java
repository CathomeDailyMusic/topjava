package ru.javawebinar.topjava.repository.jdbc;

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
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;

import static ru.javawebinar.topjava.MealTestData.MEALS_USER;
import static ru.javawebinar.topjava.MealTestData.assertMatch;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class JdbcMealRepositoryTest {


    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealRepository repository;

    @Test
    public void save() {
        // new meal -->
        Meal newMeal = new Meal(LocalDateTime.now(), "test", 400);
        Meal created = repository.save(newMeal, USER_ID);
        newMeal.setId(created.getId());
        assertMatch(created, newMeal);
        // <--

        // update meal -->
        Meal updated = repository.get(MEALS_USER.get(0).getId(), USER_ID);
        updated.setDescription("test update");
        Meal saved = repository.save(updated, USER_ID);
        assertMatch(saved, updated);
        // <--
    }

    @Test(expected = DataAccessException.class)
    public void saveDuplicate() {
        Meal meal       = repository.get(MEALS_USER.get(0).getId(), USER_ID);
        Meal duplicate  = new Meal(meal);
        duplicate.setDescription("duplicate dateTime");
        duplicate.setCalories(0);
        repository.save(duplicate, USER_ID);
    }

    public void saveAnother() {
        Meal updated = repository.get(MEALS_USER.get(0).getId(), USER_ID);
        updated.setDescription("update another");
        assertMatch(repository.save(updated, ADMIN_ID), null);
    }

    @Test
    public void delete() {
        repository.delete(MEALS_USER.get(0).getId(), USER_ID);
        assertMatch(repository.getAll(USER_ID), MEALS_USER.subList(1, MEALS_USER.size()));
    }

    public void deleteAnother() {
        assert(!repository.delete(MEALS_USER.get(0).getId(), ADMIN_ID));
    }

    @Test
    public void get() {
        Meal meal = repository.get(MEALS_USER.get(0).getId(), USER_ID);
        assertMatch(meal, MEALS_USER.get(0));
    }

    public void getAnother() {
        assertMatch(repository.get(MEALS_USER.get(0).getId(), ADMIN_ID), null);
    }

    @Test
    public void getAll() {
        Iterable<Meal> meals = repository.getAll(USER_ID);
        assertMatch(meals, MEALS_USER);
    }

    @Test
    public void getBetween() {
        Iterable<Meal> meals = repository.getBetween(LocalDateTime.of(2019, 6, 3, 9, 0), LocalDateTime.of(2019, 6, 3, 15, 0), USER_ID);
        assertMatch(meals, MEALS_USER.subList(4, MEALS_USER.size()));
    }
}