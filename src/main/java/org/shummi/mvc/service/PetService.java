package org.shummi.mvc.service;

import org.shummi.mvc.pet.Pet;
import org.shummi.mvc.pet.model.OwnerPetDto;
import org.shummi.mvc.pet.model.PetDto;
import org.shummi.mvc.user.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PetService {
    private final Map<Long, Pet> pets;
    private long nextId;
    private final UserService userService;

    public PetService(UserService userService) {
        this.pets = new HashMap<>();
        this.userService = userService;
    }

    public Pet createPet(Long userId, PetDto request) {
        User user = userService.getUserById(userId);
        Pet pet = request.toEntity();
        pet.setId(nextId++);
        pet.setUserId(userId);
        pets.put(pet.id(),pet);
        user.addPet(pet);

        return pet;
    }

    public Pet updatePet(Long userId, Long id, PetDto request) {
        OwnerPetDto ownerPet = getUserAndCheckAccessToThemAll(userId,id);
        Pet pet = request.toEntity();
        pets.replace(id, pet);
        ownerPet.owner().updatePet(pet);

        return pet;
    }

    public Pet getPetById(Long userId, Long id) {
        OwnerPetDto ownerPet = getUserAndCheckAccessToThemAll(userId, id);

        return ownerPet.pet();
    }

    public List<Pet> getAllPets(Long userId) {
        userService.exists(userId);
        return pets.values().stream()
                .filter(pet -> pet.userId().equals(userId))
                .toList();
    }

    public void deleteById(Long userId, Long id) {
        OwnerPetDto ownerPet = getUserAndCheckAccessToThemAll(userId, id);
        Pet removed = pets.remove(id);
        ownerPet.owner().removePet(removed);
    }

    private OwnerPetDto getUserAndCheckAccessToThemAll(Long userId, Long id) {
        User user = userService.getUserById(userId);
        Pet pet = Optional.ofNullable(pets.get(id))
                .orElseThrow(() -> new NoSuchElementException("Pet not found ID: " + id));

        return new OwnerPetDto(user, pet);
    }
}
