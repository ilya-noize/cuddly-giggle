package org.shummi.mvc.controller;

import jakarta.validation.Valid;
import org.shummi.mvc.user.model.UserDto;
import org.shummi.mvc.user.User;
import org.shummi.mvc.service.UserService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User create(@RequestBody @Valid UserDto request) {
        return userService.createUser(request);
    }

    @PutMapping
    public User update(@RequestBody @Valid UserDto request) {
        return userService.updateUser(request);
    }

    @GetMapping("{id}")
    public User get(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping
    public List<User> getAll() {
        return userService.getAllUsers();
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        userService.deleteById(id);
    }

    //todo Ensure you have unit tests for each of these endpoints.
}
