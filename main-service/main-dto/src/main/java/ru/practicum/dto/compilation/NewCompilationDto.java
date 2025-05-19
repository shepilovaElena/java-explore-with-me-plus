package ru.practicum.dto.compilation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewCompilationDto {

    private Set<Long> events;

    private Boolean pinned = false;

    @NotBlank
    @Size(min = 1, max = 255)
    private String title;
}
