package org.shummi.mvc.service;

import org.shummi.mvc.user.User;
import org.shummi.mvc.user.model.UserDto;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {
    private final Map<Long, User> users = new HashMap<>();
    private long nextId;

    public User createUser(UserDto request) {
        User user = request.toEntity();
        user.setId(nextId++);
        users.put(user.id(), user);
        return user;
    }

    public User updateUser(UserDto request) {
        Long id = request.id();
        exists(id);
        User entity = request.toEntity();
        users.put(id, entity);

        return entity;
    }

    public User getUserById(Long id) {
        return Optional.ofNullable(users.get(id))
                .orElseThrow(() -> new NoSuchElementException("User Not Found ID: " + id));
    }

    public List<User> getAllUsers() {
        return users.values().stream().toList();
    }

    public void deleteById(Long id) {
        Optional.ofNullable(users.remove(id))
                .orElseThrow(() -> new NoSuchElementException("User Not Found ID: " + id));
    }

    public void exists(Long id) {
        if (!users.containsKey(id)) {
            throw new NoSuchElementException("User not found ID: " + id);
        }
    }
}