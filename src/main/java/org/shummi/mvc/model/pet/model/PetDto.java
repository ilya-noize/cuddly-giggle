package org.shummi.mvc.model.pet.model;

import jakarta.validation.constraints.NotBlank;
import org.shummi.mvc.model.pet.Pet;

public record PetDto(
        @NotBlank
        String name
) {
    public Pet toEntity() {
        return new Pet(null, name, null);
    }
}
