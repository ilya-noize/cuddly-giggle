package org.shummi.mvc.controller;

import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shummi.mvc.model.pet.Pet;
import org.shummi.mvc.model.pet.model.PetDto;
import org.shummi.mvc.service.PetService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class PetController {
    private static final Logger log = LogManager.getLogger(PetController.class);
    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping("{userId}/pets")
    public Pet create(
            @PathVariable Long userId,
            @RequestBody @Valid PetDto request
    ) {
        log.info("Creating new pet: {} for user by ID: {}", request, userId);

        return petService.createPet(userId, request);
    }

    @PutMapping("{userId}/pets/{petId}")
    public Pet update(
            @PathVariable Long userId,
            @PathVariable Long petId,
            @RequestBody @Valid PetDto request
    ) {
        log.debug("Updating existing pets={} by ID={} with owner by ID={}", request, petId, userId);
        return petService.updatePet(userId, petId, request);
    }

    @GetMapping("{userId}/pets/{petId}")
    public Pet get(
            @PathVariable Long userId,
            @PathVariable Long petId
    ) {
        log.debug("Getting pet by ID={} and its' owners's ID={}", petId, userId);

        return petService.getPetById(userId, petId);
    }

    @GetMapping("{userId}/pets")
    public List<Pet> getAll(
            @PathVariable Long userId
    ) {
        log.debug("Getting all pets by owner's ID={}", userId);

        return petService.getAllPets(userId);
    }

    @DeleteMapping("{userId}/pets/{petId}")
    public void delete(
            @PathVariable Long userId,
            @PathVariable Long petId
    ) {
        log.debug("Deleting pet by ID={} and its' owners's ID={}", petId, userId);
        petService.deleteById(userId, petId);
    }
}
