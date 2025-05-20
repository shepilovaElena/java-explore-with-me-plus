package ru.practicum.dto.compilation;

import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
@Getter
public class UpdateCompilationRequest {

    private Set<Long> events;

    private Boolean pinned;

    private String title;
}
