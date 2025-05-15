package ru.practicum.user;

public class UserMapper {

    public static  User toModel(UserCreateDto userCreateDto) {
        return User.builder()
                .email(userCreateDto.getEmail())
                .name(userCreateDto.getName())
                .build();
    }

    public static UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public static UserShortDto toShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
