package com.example.demo.repository;
import com.example.demo.model.User;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryUserRepository implements UserRepository {

    private final Map<Long, User> store;
    private final AtomicLong idGenerator = new AtomicLong();

    public InMemoryUserRepository() {
        store = new ConcurrentHashMap<>();
        store.put(1L, new User(1L, "John Doe1", "john.doe1@email.com", "password123"));
        store.put(2L, new User(2L, "John Doe2", "john.doe2@email.com", "password123"));
        store.put(3L, new User(3L, "John Doe3", "john.doe3@email.com", "password123"));
        store.put(4L, new User(4L, "John Doe4", "john.doe4@email.com", "password123"));

        idGenerator.set(4);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(idGenerator.incrementAndGet());
        }
        store.put(user.getId(), user);
        return user;
    }

    @Override
    public boolean deleteById(Long id) {
        return store.remove(id) != null;
    }
}

