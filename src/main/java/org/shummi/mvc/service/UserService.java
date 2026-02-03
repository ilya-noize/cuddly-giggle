package org.shummi.mvc.service;

import org.shummi.mvc.exception.MvpException;
import org.shummi.mvc.user.User;
import org.shummi.mvc.user.model.UserDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private final Map<Long, User> users;
    private final Set<String> emails;
    private long nextId;

    UserService() {
        this.users = new HashMap<>();
        this.emails = new HashSet<>();
        this.nextId = 0L;
    }

    public User createUser(UserDto request) {
        User user = request.toEntity();
        if (emails.contains(request.email())) {
            throw new MvpException("Duplicate email");
        }
        user.setId(++nextId);
        user.setPets(new ArrayList<>());
        users.put(user.getId(), user);
        emails.add(user.getEmail());

        return user;
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
        return users.values().stream().toList();
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