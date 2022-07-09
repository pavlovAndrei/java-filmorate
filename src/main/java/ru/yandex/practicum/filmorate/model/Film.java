package ru.yandex.practicum.filmorate.model;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Objects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class Film {

    private int id;
    @NotNull
    @NotBlank
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Duration duration;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return getName().equals(film.getName())
                && getDescription().equals(film.getDescription())
                && getReleaseDate().equals(film.getReleaseDate())
                && getDuration().equals(film.getDuration());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription(), getReleaseDate(), getDuration());
    }
}
