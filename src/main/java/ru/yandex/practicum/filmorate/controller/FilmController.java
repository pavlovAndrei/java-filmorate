package ru.yandex.practicum.filmorate.controller;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
import ru.yandex.practicum.filmorate.model.Film;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Set<Film> films = new HashSet<>();

    @GetMapping()
    public Set<Film> findAll() {
        log.debug("Films count is: {}", films.size());
        return films;
    }

    @PostMapping()
    public Film create(@Valid @RequestBody Film film) {
        if (films.contains(film)) {
            throw new ObjectAlreadyExistException("Film already exists.");
        } else {
            validateFilm(film);
            film.setId(films.size() + 1);
            films.add(film);
            log.debug("Film is added: {}", film);
            return film;
        }
    }

    @PutMapping()
    public Film saveFilm(@Valid @RequestBody Film film) {
        validateFilm(film);
        if (!films.contains(film)) {
            film.setId(films.size() + 1);
        }
        films.add(film);
        log.debug("Film is updated: {}", film);
        return film;
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Film must contain name.");
        } else if (film.getDescription().length() > 200) {
            throw new ValidationException("Film description must be not more than 200 characters.");
        } else if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
            throw new ValidationException("Film release date must be not earlier than Dec 28th 1895.");
        } else if (film.getDuration().isNegative()) {
            throw new ValidationException("Film duration must be positive.");
        }
    }
}