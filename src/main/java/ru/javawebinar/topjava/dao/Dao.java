package ru.javawebinar.topjava.dao;

import java.util.List;

public interface Dao<T> {
    void add(T item);

    void delete(long id);

    void update(T item);

    List<T> getAll();

    T getById(long id);
}
