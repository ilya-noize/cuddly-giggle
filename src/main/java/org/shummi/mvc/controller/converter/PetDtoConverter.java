package org.shummi.mvc.controller.converter;

import org.shummi.mvc.model.pet.Pet;
import org.shummi.mvc.model.pet.PetRequestDto;
import org.shummi.mvc.model.pet.PetResponseDto;
import org.springframework.stereotype.Component;

@Component
public class PetDtoConverter {

    public Pet toEntity(PetRequestDto dto) {
        return new Pet(
                null,
                dto.name(),
                null
        );
    }

    public PetResponseDto toDto(Pet entity) {
        return new PetResponseDto(
                entity.id(),
                entity.name()
        );
    }
}
