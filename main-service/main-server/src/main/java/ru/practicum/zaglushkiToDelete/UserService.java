package ru.practicum.zaglushkiToDelete;

public interface UserService {
    Optional<UserDto> findUserById(int id);
}
