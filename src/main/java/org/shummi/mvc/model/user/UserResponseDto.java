package org.shummi.mvc.model.user;

import org.shummi.mvc.model.pet.PetResponseDto;

import java.util.List;

public record UserResponseDto(
        Long id,
        String name,
        String email,
        Integer age,
        List<PetResponseDto> pets
) {
}
