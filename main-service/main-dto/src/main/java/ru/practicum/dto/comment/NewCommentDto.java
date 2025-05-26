package ru.practicum.dto.comment;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewCommentDto {
    @NotBlank
    @Size(min = 3, max = 3000)
    String content;

    @NotNull
    @Positive
    Long user;

    @NotNull
    @Positive
    Long event;
}
