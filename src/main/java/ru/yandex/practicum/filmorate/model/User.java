package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.Objects;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class User {

    private int id;
    @NotNull
    @NotBlank
    @Email
    private String email;
    @NotNull
    @NotBlank
    private String login;
    private String name;
    private LocalDate birthday;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getEmail().equals(user.getEmail())
                && getLogin().equals(user.getLogin())
                && getName().equals(user.getName())
                && getBirthday().equals(user.getBirthday());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail(), getLogin(), getName(), getBirthday());
    }
}
