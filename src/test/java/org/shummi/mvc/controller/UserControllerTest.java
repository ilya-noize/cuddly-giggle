package org.shummi.mvc.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.shummi.mvc.service.UserService;
import org.shummi.mvc.user.User;
import org.shummi.mvc.user.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mock;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserService userService;

    private UserDto userDto;
    private User createdUser;

    @BeforeEach
    void setUp() {
        String name = UUID.randomUUID().toString().substring(0, 8);
        String email = name + "e@mail.com";
        int age = Integer.decode(name) % 100;
        UserDto userDto = new UserDto(name, email, age);
        createdUser = userService.createUser(userDto);
    }

    @Test
    @DisplayName("POST: successfully create a valid entity")
    void post_shouldSuccessfullyCreateUser() throws Exception {
        ResultActions performed = mock.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)));
        String json = performed.andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(json).isNotBlank();
        createdUser = objectMapper.readValue(json, User.class);

        Assertions.assertNotNull(createdUser);
        assertThat(createdUser.getId()).isNotNull();
        assertThat(createdUser.getName()).isEqualTo(userDto.name());
        assertThat(createdUser.getEmail()).isEqualTo(userDto.email());
        assertThat(createdUser.getAge()).isEqualTo(userDto.age());
        assertThat(createdUser.getPets()).isEqualTo(new ArrayList<>());
    }

    @Test
    @DisplayName("POST: bad request when name is blank")
    void post_shouldBadRequestWhenCreateUserGetNameIsBlank() throws Exception {
        var dto = new UserDto(" ", "mail0002@mail.com", 12);

        String json = objectMapper.writeValueAsString(dto);
        mock.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST: bad request when email is invalid")
    void post_shouldBadRequestWhenCreateUserEmailIsInvalided() throws Exception {
        var dto = new UserDto("name", "user/127.0.0.1:8080", null);

        String json = objectMapper.writeValueAsString(dto);
        mock.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST: bad request when age is null")
    void post_shouldBadRequestWhenCreateUserAgeIsNull() throws Exception {
        var dto = new UserDto("name", "mail0002@mail.com", null);

        String json = objectMapper.writeValueAsString(dto);
        mock.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET: successfully get empty list of the users")
    void get_shouldSuccessfullyGetUsersEmptyList() throws Exception {
        String content = mock.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<User> users = objectMapper.readerForListOf(User.class).readValue(content);
        Assertions.assertTrue(users == null || !users.isEmpty());
    }

    @Test
    @DisplayName("GET: successfully get user by id")
    void get_shouldSuccessfullyGetUserByGetId() throws Exception {
        ResultActions resultActions = mock.perform(get("/api/v1/users/{id}", createdUser.getId())
                .contentType(MediaType.APPLICATION_JSON));
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdUser.getId()))
                .andExpect(jsonPath("$.name").value(createdUser.getName()))
                .andExpect(jsonPath("$.email").value(createdUser.getEmail()))
                .andExpect(jsonPath("$.age").value(createdUser.getAge()));
    }

    @Test
    @DisplayName("GET: not fount user with invalid ID.")
    void get_shouldNotFoundUserByWrongGetId() throws Exception {
        Integer id = Integer.MAX_VALUE;
        mock.perform(get("/api/v1/users/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT: successfully update an existing record.")
    void put_shouldSuccessfullyUpdateExistingEntity() throws Exception {
        var updateDto = new UserDto("updated-name", createdUser.getEmail(), createdUser.getAge());

        String json = objectMapper.writeValueAsString(updateDto);
        ResultActions performed = mock.perform(put("/api/v1/users/{id}", createdUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
        performed.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updateDto.name()))
                .andExpect(jsonPath("$.email").value(createdUser.getEmail()))
                .andExpect(jsonPath("$.age").value(createdUser.getAge()));
    }

    @Test
    @DisplayName("PUT: bad request on update invalid data")
    void put_shouldReturnedBadRequestOnInvalidDataInPutMethod() throws Exception {
        var invalidDto = new UserDto(" ", createdUser.getEmail(), createdUser.getAge());

        String json = objectMapper.writeValueAsString(invalidDto);
        mock.perform(put("/api/v1/users/{id}", createdUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT: bad Request when updating email.")
    void shouldReturnedBadRequestIfChangeEmailInPutMethod() throws Exception {
        var dto = new UserDto(createdUser.getName(), "other-email@email.ru", createdUser.getAge());

        String json = objectMapper.writeValueAsString(dto);
        mock.perform(put("/api/v1/users/{id}", createdUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }
}
