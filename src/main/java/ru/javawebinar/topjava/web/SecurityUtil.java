package ru.javawebinar.topjava.web;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class SecurityUtil {

    // null if user is not logged in
    private static Integer authUserId;

    public static Integer authUserId() {
        return authUserId;
    }

    public static void setAuthUserId(Integer id) {
        authUserId = id;
    }

    public static int authUserCaloriesPerDay() {
        return DEFAULT_CALORIES_PER_DAY;
    }
}