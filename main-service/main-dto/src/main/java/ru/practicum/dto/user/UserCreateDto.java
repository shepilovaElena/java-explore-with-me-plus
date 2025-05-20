package ru.practicum.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateDto {
    @NotBlank
    @Email
    @Size(min = 6, max = 254, message = "от 6 до 254")
    private String email;
    @NotBlank
    @Size(min = 2, max = 251, message = "от 2 до 250")
    private String name;
}
