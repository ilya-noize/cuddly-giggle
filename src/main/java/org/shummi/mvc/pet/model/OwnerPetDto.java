package org.shummi.mvc.pet.model;

import org.shummi.mvc.pet.Pet;
import org.shummi.mvc.user.User;

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
