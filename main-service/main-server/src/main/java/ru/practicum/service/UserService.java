package ru.practicum.service;

public interface UserService {
    Optional<UserDto> findUserById(int id);
}
