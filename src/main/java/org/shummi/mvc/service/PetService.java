package org.shummi.mvc.service;

import org.shummi.mvc.model.pet.OwnerPetDto;
import org.shummi.mvc.model.pet.Pet;
import org.shummi.mvc.model.user.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PetService {
    private final Map<Long, Pet> pets;
    private final AtomicLong nextId;
    private final UserService userService;

    public PetService(UserService userService) {
        this.pets = new ConcurrentHashMap<>();
        this.userService = userService;
        this.nextId = new AtomicLong(0);
    }

    public Pet createPet(Long userId, Pet pet) {
        User user = userService.getUserById(userId);
        pet.setId(nextId.incrementAndGet());
        pet.setUserId(userId);

        user.addPet(pet);
        pets.put(pet.id(), pet);

        return pet;
    }

    public Pet updatePet(Long userId, Long id, Pet pet) {
        OwnerPetDto ownerPet = getUserAndCheckAccessToThemAll(userId, id);
        User owner = ownerPet.owner();
        Pet existingPet = ownerPet.pet();

        if (existingPet.name().equals(pet.name())) {
            return existingPet;
        }

        Pet updatedPet = new Pet(id, pet.name(), userId);
        owner.updatePet(updatedPet);
        pets.replace(id, updatedPet);

        return updatedPet;
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
        User owner = ownerPet.owner();
        Pet pet = ownerPet.pet();
        owner.removePet(pet);
        pets.remove(id);
    }

    private OwnerPetDto getUserAndCheckAccessToThemAll(Long userId, Long id) {
        User user = userService.getUserById(userId);
        Pet pet = Optional.ofNullable(pets.get(id))
                .orElseThrow(() -> new NoSuchElementException("Pet not found ID: " + id));

        return new OwnerPetDto(user, pet);
    }
}
