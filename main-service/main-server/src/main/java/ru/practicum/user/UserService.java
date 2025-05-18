package ru.practicum.user;


import java.util.List;

public interface UserService {

    UserDto createUser(UserDto user);

    List<UserDto> getUsers(List<Long> ids, Integer from, Integer size);

    void deleteUserById(Long id);
}
