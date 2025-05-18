package ru.practicum.user;

import org.mapstruct.Mapper;
import ru.practicum.dto.user.UserDto;
import ru.practicum.dto.user.UserShortDto;


@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User user);

    User toUser(UserDto userDto);

    UserShortDto toUserShortDto(User user);
}
