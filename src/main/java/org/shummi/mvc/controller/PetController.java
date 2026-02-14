package org.shummi.mvc.controller;

import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shummi.mvc.controller.converter.PetDtoConverter;
import org.shummi.mvc.model.pet.PetRequestDto;
import org.shummi.mvc.model.pet.PetResponseDto;
import org.shummi.mvc.service.PetService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PetController {
    private static final Logger log = LogManager.getLogger(PetController.class);
    private final PetService petService;
    private final PetDtoConverter dtoConverter;

    public PetController(PetService petService, PetDtoConverter dtoConverter) {
        this.petService = petService;
        this.dtoConverter = dtoConverter;
    }

    @PostMapping("/api/v1/users/{userId}/pets")
    public ResponseEntity<PetResponseDto> create(
            @PathVariable Long userId,
            @RequestBody @Valid PetRequestDto request
    ) {
        log.info("Creating new pet: {} for user by ID: {}", request, userId);

        return ResponseEntity
                .status(201)
                .contentType(MediaType.APPLICATION_JSON)
                .body(dtoConverter.toDto(petService.createPet(
                        userId,
                        dtoConverter.toEntity(request)
                )));
    }

    @PutMapping("/api/v1/users/{userId}/pets/{petId}")
    public PetResponseDto update(
            @PathVariable Long userId,
            @PathVariable Long petId,
            @RequestBody @Valid PetRequestDto request
    ) {
        log.debug("Updating existing pets={} by ID={} with owner by ID={}", request, petId, userId);
        return dtoConverter.toDto(
                petService.updatePet(
                        userId,
                        petId,
                        dtoConverter.toEntity(request)
                )
        );
    }

    @GetMapping("/api/v1/users/{userId}/pets/{petId}")
    public PetResponseDto get(
            @PathVariable Long userId,
            @PathVariable Long petId
    ) {
        log.debug("Getting pet by ID={} and its' owners's ID={}", petId, userId);

        return dtoConverter.toDto(petService.getPetById(userId, petId));
    }

    @GetMapping("/api/v1/users/{userId}/pets")
    public List<PetResponseDto> getAll(
            @PathVariable Long userId
    ) {
        log.debug("Getting all pets by owner's ID={}", userId);

        return petService.getAllPets(userId).stream()
                .map(dtoConverter::toDto)
                .toList();
    }

    @DeleteMapping("/api/v1/users/{userId}/pets/{petId}")
    public void delete(
            @PathVariable Long userId,
            @PathVariable Long petId
    ) {
        log.debug("Deleting pet by ID={} and its' owners's ID={}", petId, userId);
        petService.deleteById(userId, petId);
    }
}
