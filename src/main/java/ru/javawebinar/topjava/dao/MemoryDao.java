package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Idable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MemoryDao<T extends Idable> implements Dao<T> {
    private final AtomicLong counter;
    private final Map<Long, T> map;

    public MemoryDao() {
        map = new ConcurrentHashMap<>();
        counter = new AtomicLong(0L);
    }

    @Override
    public void add(T item) {
        long id = counter.incrementAndGet();
        item.setId(id);
        map.put(id, item);
    }

    @Override
    public void delete(long id) {
        map.remove(id);
    }

    @Override
    public void update(T item) {
        map.put(item.getId(), item);
    }

    @Override
    public List<T> getAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    public T getById(long id) {
        return map.get(id);
    }
}