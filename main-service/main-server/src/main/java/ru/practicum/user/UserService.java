package ru.practicum.user;

import java.util.List;

public interface UserService {

    UserDto createUser(UserCreateDto userCreateDto);

    List<UserDto> getUsers(int from, int size);

    List<UserDto> getUsers(List<Long> usersList);

    void deleteUserById(long id);
}
