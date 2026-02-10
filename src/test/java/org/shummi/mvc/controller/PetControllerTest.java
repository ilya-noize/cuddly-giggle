package org.shummi.mvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.shummi.mvc.model.pet.Pet;
import org.shummi.mvc.model.pet.model.PetDto;
import org.shummi.mvc.service.PetService;
import org.shummi.mvc.service.UserService;
import org.shummi.mvc.model.user.User;
import org.shummi.mvc.model.user.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PetControllerTest {
    @Autowired
    private MockMvc mock;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private PetService petService;

    private User existingUser;


    @BeforeEach
    void setUp() {
        String name = UUID.randomUUID().toString().substring(0, 8);
        String email = name + "e@mail.com";
        UserDto userDto = new UserDto(name, email, 22);
        existingUser = userService.createUser(userDto);
    }

    @Test
    @Disabled("No static resource api/v1/users/1/pets.")
    @DisplayName("POST: should create a valid pets")
    void post_shouldReturnPet() throws Exception {
        PetDto petDto = new PetDto("cat");

        String response = mock.perform(post("/api/v1/users/{userId}/pets/", existingUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(petDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(response).isNotBlank();
        Pet createPet = objectMapper.readValue(response, Pet.class);

        assertThat(createPet.id()).isNotNull();
        assertThat(createPet.userId()).isEqualTo(existingUser.getId());
        assertThat(createPet.name()).isEqualTo(petDto.name());
    }

    @Test
    @DisplayName("POST: not found user by ID")
    void post_shouldNotFoundByInvalidUserId() throws Exception {
        Long invalidUserId = Long.MAX_VALUE;
        mock.perform(post("/api/v1/users/{userId}/pets/", invalidUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new PetDto("cat"))))
                .andExpect(status().isNotFound());
    }

    @Test
    @Disabled("No static resource api/v1/users/2/pets.")
    @DisplayName("POST: should return Bad Request when pet name is blank")
    void post_shouldBadRequestWhenEmptyBody() throws Exception {
        PetDto petDto = new PetDto(" ");

        mock.perform(post("/api/v1/users/{userId}/pets/", existingUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(petDto)))
                .andExpect(status().isBadRequest());
    }
}