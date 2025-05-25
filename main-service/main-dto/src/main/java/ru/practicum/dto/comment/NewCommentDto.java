package ru.practicum.dto.comment;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewCommentDto {
    @NotBlank
    String content;

    @NotNull
    @Positive
    Long user;

    @NotNull
    @Positive
    Long event;
}
