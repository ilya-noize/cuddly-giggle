package org.shummi.mvc.model.pet.model;

import org.shummi.mvc.model.pet.Pet;
import org.shummi.mvc.model.user.User;

import java.util.Objects;

public record OwnerPetDto(
        User owner,
        Pet pet
) {
    public OwnerPetDto {
        if (!Objects.equals(owner.getId(), pet.userId())) {
            throw new IllegalArgumentException("Owner and user must be the same");
        }
    }
}
