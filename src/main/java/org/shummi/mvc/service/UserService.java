package org.shummi.mvc.service;

import org.shummi.mvc.exception.MvpException;
import org.shummi.mvc.model.user.User;
import org.shummi.mvc.model.user.model.UserDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {
    private final Map<Long, User> users;
    private final Set<String> emails;
    private final AtomicLong nextId;

    UserService() {
        this.users = new ConcurrentHashMap<>();
        this.emails = new ConcurrentSkipListSet<>();
        this.nextId = new AtomicLong(0);
    }

    public User createUser(UserDto request) {
        boolean added = emails.add(request.email());
        if (!added) {
            throw new MvpException("Duplicate email");
        }
        try {
            User user = request.toEntity();
            user.setId(nextId.incrementAndGet());
            user.setPets(new ArrayList<>());
            users.put(user.getId(), user);

            return user;
        } catch (Exception e) {
            // rollback on error
            emails.remove(request.email());
            throw e;
        }
    }

    public User updateUser(Long id, UserDto request) {
        User user = getUserById(id);
        User entity = request.toEntity();
        if (!entity.getEmail().equals(user.getEmail())) {
            throw new MvpException("Email cannot be changed");
        }
        entity.setId(id);
        entity.setPets(user.getPets());
        users.put(id, entity);

        return entity;
    }

    public User getUserById(Long id) {
        return Optional.ofNullable(users.get(id))
                .orElseThrow(() -> new NoSuchElementException("User Not Found ID: " + id));
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public void deleteById(Long id) {
        User user = Optional.ofNullable(users.remove(id))
                .orElseThrow(() -> new NoSuchElementException("User Not Found ID: " + id));
        emails.remove(user.getEmail());
    }

    public void exists(Long id) {
        if (!users.containsKey(id)) {
            throw new NoSuchElementException("User not found ID: " + id);
        }
    }
}