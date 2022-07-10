package ru.yandex.practicum.filmorate.controller;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping()
    public Collection<User> findAll() {
        log.debug("Users count is: {}", users.size());
        return users.values();
    }

    @PostMapping()
    public User create(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            throw new ObjectAlreadyExistException("User already exists.");
        } else {
            validateUser(user);
            User newUser = validateUserName(user);
            newUser.setId(users.size() + 1);
            users.put(newUser.getId(), newUser);
            log.debug("User is added: {}", newUser);
            return newUser;
        }
    }

    @PutMapping()
    public User saveUser(@Valid @RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("User not found.");
        }
        validateUser(user);
        User newUser = validateUserName(user);
        users.put(newUser.getId(), newUser);
        log.debug("User is updated: {}", newUser);
        return newUser;
    }

    private void validateUser(User user) {
        if (isNotBlank(user.getEmail())) {
            throw new ValidationException("User must contain email.");
        } else if (!user.getEmail().contains("@")) {
            throw new ValidationException("User must contain @ symbol.");
        } else if (isNotBlank(user.getLogin())) {
            throw new ValidationException("User must contain login.");
        } else if (user.getLogin().contains(" ")) {
            throw new ValidationException("User login must not contain whitespaces.");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("User's birthday cannot be in the future.");
        }
    }

    private User validateUserName(User user) {
        if (isNotBlank(user.getName())) {
            user.setName(user.getLogin());
        }
        return user;
    }
}
