package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Idable;
import ru.javawebinar.topjava.util.Counter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MemoryDao<T extends Idable> implements Dao<T> {
    private List<T> list;
    private Counter counter;

    public MemoryDao() {
        list = new CopyOnWriteArrayList<>();
        counter = new Counter();
    }

    @Override
    public void add(T item) {
        item.setId(counter.incrementAndGet());
        list.add(item);
    }

    @Override
    public void delete(long id) {
        T found = getById(id);
        list.remove(found);
    }

    @Override
    public void update(T item) {
        list.set(list.indexOf(item), item);
    }

    @Override
    public List<T> getAll() {
        return list;
    }

    @Override
    public T getById(long id) {
        for (T item : list) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }
}