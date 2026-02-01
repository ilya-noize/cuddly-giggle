package org.shummi.mvc.controller;

import jakarta.validation.Valid;
import org.shummi.mvc.pet.Pet;
import org.shummi.mvc.pet.model.PetDto;
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
    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping("{userId}/pets")
    public Pet create(
            @PathVariable Long userId,
            @RequestBody @Valid PetDto request
    ) {
        return petService.createPet(userId, request);
    }

    @PutMapping("{userId}/pets/{id}")
    public Pet update(
            @PathVariable Long userId,
            @PathVariable Long id,
            @RequestBody @Valid PetDto request
    ) {
        return petService.updatePet(userId, id, request);
    }

    @GetMapping("{userId}/pets/{id}")
    public Pet get(
            @PathVariable Long userId,
            @PathVariable Long id
    ) {
        return petService.getPetById(userId, id);
    }

    @GetMapping("{userId}/pets")
    public List<Pet> getAll(
            @PathVariable Long userId
    ) {
        return petService.getAllPets(userId);
    }

    @DeleteMapping("{userId}/pets/{id}")
    public void delete(
            @PathVariable Long userId,
            @PathVariable Long id
    ) {
        petService.deleteById(userId, id);
    }

    //todo Ensure you have unit tests for each of these endpoints.
}
