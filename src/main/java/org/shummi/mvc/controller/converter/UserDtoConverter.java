package org.shummi.mvc.controller.converter;

import org.shummi.mvc.model.user.User;
import org.shummi.mvc.model.user.UserRequestDto;
import org.shummi.mvc.model.user.UserResponseDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class UserDtoConverter {
    private final PetDtoConverter petDtoConverter;

    public UserDtoConverter(PetDtoConverter petDtoConverter) {
        this.petDtoConverter = petDtoConverter;
    }

    public User toEntity(UserRequestDto dto) {
        return new User(
                null,
                dto.name(),
                dto.email(),
                dto.age(),
                null
        );
    }

    public UserResponseDto toDto(User entity) {

        return new UserResponseDto(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getAge(),
                entity.getPets() == null || entity.getPets().isEmpty()
                        ? new ArrayList<>()
                        : entity.getPets().stream()
                        .map(petDtoConverter::toDto)
                        .toList()
        );
    }
}
