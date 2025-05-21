package ru.practicum.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateDto {
    @NotBlank
    @Email
    @Size(min = 6, max = 254, message = "от 6 до 254")
    String email;
    @NotBlank
    @Size(min = 2, max = 250, message = "от 2 до 250")
    String name;
}
