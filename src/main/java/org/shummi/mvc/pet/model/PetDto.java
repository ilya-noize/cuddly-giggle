package org.shummi.mvc.pet.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import org.shummi.mvc.pet.Pet;

public record PetDto(
        @Null
        Long id,

        @NotBlank
        String name,

        @NotNull
        Long userId
) {
    public Pet toEntity() {
        return new Pet(id, name, userId);
    }
}
