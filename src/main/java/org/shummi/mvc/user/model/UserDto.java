package org.shummi.mvc.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import org.shummi.mvc.pet.Pet;
import org.shummi.mvc.pet.model.PetDto;
import org.shummi.mvc.user.User;

import java.util.List;

public record UserDto(
        @Null
        Long id,

        @NotBlank
        String name,

        @Email
        @NotBlank
        String email,

        @NotNull
        @Positive
        Integer age,

        @Null
        List<PetDto> pets
) {
    public User toEntity() {
        List<Pet> petList = pets.stream().map(PetDto::toEntity).toList();
        return new User(id, name, email, age, petList);
    }
}
