package org.shummi.mvc.service;

import jakarta.validation.Valid;
import org.shummi.mvc.pet.Pet;
import org.shummi.mvc.user.model.UserDto;
import org.shummi.mvc.user.User;
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

    public User createUser(UserDto userDto) {
        long id = this.nextId++;
        User entity = userDto.toEntity();
        entity.setId(id);
        users.put(id, entity);
        return entity;
    }

    public User updateUser(@Valid UserDto request) {
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

    public void addPets(Pet pet) {
        users.get(pet.userId()).addPet(pet);
    }

    public void updatePet(Pet pet) {
        users.get(pet.userId()).updatePet(pet);
    }

    public void removePet(Pet pet) {
        users.get(pet.userId()).removePet(pet);
    }

}