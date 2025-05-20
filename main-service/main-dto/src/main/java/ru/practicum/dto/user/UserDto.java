package ru.practicum.dto.user;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    Long id;
    String email;
    String name;
}
