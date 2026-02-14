package org.shummi.mvc.controller;

import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shummi.mvc.controller.converter.UserDtoConverter;
import org.shummi.mvc.model.user.UserRequestDto;
import org.shummi.mvc.model.user.UserResponseDto;
import org.shummi.mvc.service.UserService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
public class UserController {
    private static final Logger log = LogManager.getLogger(UserController.class);
    private final UserService userService;
    private final UserDtoConverter dtoConverter;

    public UserController(
            UserService userService,
            UserDtoConverter dtoConverter
    ) {
        this.userService = userService;
        this.dtoConverter = dtoConverter;
    }

    @PostMapping("/api/v1/users")
    @ResponseStatus(CREATED)
    public UserResponseDto create(
            @RequestBody @Valid UserRequestDto request
    ) {
        log.debug("Creating user: {}", request.toString());

        return dtoConverter.toDto(userService.createUser(dtoConverter.toEntity(request)));
    }

    @PutMapping("/api/v1/users/{id}")
    public UserResponseDto update(
            @PathVariable("id") long id,
            @RequestBody @Valid UserRequestDto request
    ) {
        log.debug("Updating a user by ID={}", id);

        return dtoConverter.toDto(userService.updateUser(id, dtoConverter.toEntity(request)));
    }

    @GetMapping("/api/v1/users/{id}")
    public UserResponseDto get(
            @PathVariable Long id
    ) {
        log.debug("Getting user by ID={}", id);

        return dtoConverter.toDto(userService.getUserById(id));
    }

    @GetMapping("/api/v1/users")
    public List<UserResponseDto> getAll() {
        log.debug("Getting all users");

        return userService.getAllUsers().stream()
                .map(dtoConverter::toDto)
                .toList();
    }

    @DeleteMapping("/api/v1/users/{id}")
    public void delete(
            @PathVariable Long id
    ) {
        log.debug("Deleting user by ID={}", id);

        userService.deleteById(id);
    }
}
