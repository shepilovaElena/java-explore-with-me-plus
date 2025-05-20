package ru.practicum.dto.user;

import lombok.*;

@Data
@Getter
@Setter
//@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class UserShortDto {
    private Long id;
    private String name;
}
