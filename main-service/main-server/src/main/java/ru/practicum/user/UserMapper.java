package ru.practicum.user;

import org.mapstruct.Mapper;
import ru.practicum.dto.user.UserCreateDto;
import ru.practicum.dto.user.UserDto;
import ru.practicum.dto.user.UserShortDto;


@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User user);

    UserShortDto toUserShortDto(User user);

    User toUser(UserCreateDto userCreateDto);
}
