package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.dao.Dao;
import ru.javawebinar.topjava.dao.MemoryDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.CopyOnWriteArrayList;

public class MealServlet extends HttpServlet {
    private static String LIST = "meals.jsp";
    private Dao<Meal> dao;

    public MealServlet() {
        dao = new MemoryDao<>();
    }

    @Override
    public void init() throws ServletException {
        super.init();

        dao.add(new Meal(LocalDateTime.of(2019, 6, 1, 10, 0),
                "Завтрак", 500));
        dao.add(new Meal(LocalDateTime.of(2019, 6, 1, 14, 0),
                "Обед", 700));
        dao.add(new Meal(LocalDateTime.of(2019, 6, 1, 20, 0),
                "Ужин", 600));
        dao.add(new Meal(LocalDateTime.of(2019, 6, 2, 10, 0),
                "Завтрак", 1000));
        dao.add(new Meal(LocalDateTime.of(2019, 6, 2, 15, 0),
                "Обед", 500));
        dao.add(new Meal(LocalDateTime.of(2019, 6, 2, 21, 0),
                "Ужин", 1000));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String INSERT_OR_EDIT = "meal.jsp";
        String forward;
        String action = request.getParameter("action");
        if (action == null) {
            forward = LIST;
            request.setAttribute("meals", new CopyOnWriteArrayList<>(MealsUtil.getFilteredWithExcess(dao.getAll(),
                    LocalTime.MIN, LocalTime.MAX, 2000)));
        } else
            switch (action) {
                case "delete":
                    forward = LIST;
                    dao.delete(Integer.parseInt(request.getParameter("id")));
                    request.setAttribute("meals", new CopyOnWriteArrayList<>(MealsUtil.getFilteredWithExcess(dao.getAll(),
                            LocalTime.MIN, LocalTime.MAX, 2000)));
                    break;
                case "edit":
                    forward = INSERT_OR_EDIT;
                    Meal meal = dao.getById(Integer.parseInt(request.getParameter("id")));
                    request.setAttribute("meal", meal);
                    break;
                case "insert":
                    forward = INSERT_OR_EDIT;
                    break;
                default:
                    forward = LIST;
                    request.setAttribute("meals", new CopyOnWriteArrayList<>(MealsUtil.getFilteredWithExcess(dao.getAll(),
                            LocalTime.MIN, LocalTime.MAX, 2000)));
                    break;
            }

        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        LocalDateTime dateTime  = LocalDateTime.of(LocalDate.parse(request.getParameter("date")),
                LocalTime.parse(request.getParameter("time")));
        String description      = request.getParameter("description");
        int calories            = Integer.parseInt(request.getParameter("calories"));
        Meal meal               = new Meal(dateTime, description, calories);

        String id = request.getParameter("id");
        if (id == null || id.isEmpty()) {
            dao.add(meal);
        } else {
            meal.setId(Long.parseLong(id));
            dao.update(meal);
        }

        request.setAttribute("meals", new CopyOnWriteArrayList<>(MealsUtil.getFilteredWithExcess(dao.getAll(),
                LocalTime.MIN, LocalTime.MAX, 2000)));
        request.getRequestDispatcher(LIST).forward(request, response);
    }
}