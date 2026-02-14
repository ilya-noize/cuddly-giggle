package org.shummi.mvc.controller.converter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shummi.mvc.model.user.User;
import org.shummi.mvc.model.user.UserResponseDto;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class UserDtoConverterTest {
    private final UserDtoConverter userDtoConverter;

    UserDtoConverterTest() {
        PetDtoConverter petDtoConverter = new PetDtoConverter();
        this.userDtoConverter = new UserDtoConverter(petDtoConverter);
    }

    @Test
    void shouldConvertUserToUserDTO() {
        //given
        User user = new User(1L,
                "name",
                "email@mail.com",
                18,
                new ArrayList<>()
        );
        UserResponseDto expectedResult = new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge(),
                new ArrayList<>()
        );
        //when
        UserResponseDto result = userDtoConverter.toDto(user);
        //then
        assertEquals(expectedResult,result);
    }
}