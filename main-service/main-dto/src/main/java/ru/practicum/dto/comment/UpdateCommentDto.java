package ru.practicum.dto.comment;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateCommentDto {
    @NotNull
    @Positive
    Long id;
    @NotBlank
    String content;
}
