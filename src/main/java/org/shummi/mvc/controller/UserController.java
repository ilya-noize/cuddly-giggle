package org.shummi.mvc.controller;

import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shummi.mvc.model.user.model.UserDto;
import org.shummi.mvc.model.user.User;
import org.shummi.mvc.service.UserService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private static final Logger log = LogManager.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public User create(
            @RequestBody @Valid UserDto request
    ) {
        log.debug("Creating user: {}", request.toString());

        return userService.createUser(request);
    }

    @PutMapping("{id}")
    public User update(
            @PathVariable("id") long id,
            @RequestBody @Valid UserDto request
    ) {
        log.debug("Updating a user by ID={}", id);

        return userService.updateUser(id, request);
    }

    @GetMapping("{id}")
    public User get(
            @PathVariable Long id
    ) {
        log.debug("Getting user by ID={}", id);

        return userService.getUserById(id);
    }

    @GetMapping
    public List<User> getAll() {
        log.debug("Getting all users");

        return userService.getAllUsers();
    }

    @DeleteMapping("{id}")
    public void delete(
            @PathVariable Long id
    ) {
        log.debug("Deleting user by ID={}", id);

        userService.deleteById(id);
    }
}
