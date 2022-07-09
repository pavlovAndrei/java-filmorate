package ru.yandex.practicum.filmorate.controller;

import java.time.LocalDate;
import java.util.HashSet;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final HashSet<User> users = new HashSet<>();

    @GetMapping()
    public HashSet<User> findAll() {
        log.debug("Users count is: {}", users.size());
        return users;
    }

    @PostMapping()
    public User create(@Valid @RequestBody User user) {
        if (users.contains(user)) {
            throw new ObjectAlreadyExistException("User already exists.");
        } else {
            validateUser(user);
            User newUser = validateUserName(user);
            newUser.setId(users.size() + 1);
            users.add(newUser);
            log.debug("User is added: {}", newUser);
            return newUser;
        }
    }

    @PutMapping()
    public User saveUser(@Valid @RequestBody User user) {
        validateUser(user);
        User newUser = validateUserName(user);
        if (!users.contains(newUser)) {
            newUser.setId(users.size() + 1);
        }
        users.add(newUser);
        log.debug("User is updated: {}", newUser);
        return newUser;
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("User must contain email.");
        } else if (!user.getEmail().contains("@")) {
            throw new ValidationException("User must contain @ symbol.");
        } else if (user.getLogin() == null || user.getLogin().isBlank()) {
            throw new ValidationException("User must contain login.");
        } else if (user.getLogin().contains(" ")) {
            throw new ValidationException("User login must not contain whitespaces.");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("User's birthday cannot be in the future.");
        }
    }

    private User validateUserName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return user;
    }
}
