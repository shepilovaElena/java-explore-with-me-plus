package ru.practicum.error;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(int id) {
        super("Пользователь с id " + id + " не найден");
    }
}
