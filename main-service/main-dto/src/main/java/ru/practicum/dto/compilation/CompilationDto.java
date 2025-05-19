package ru.practicum.dto.compilation;

import lombok.*;
import ru.practicum.dto.event.EventShortDto;

import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {

    private Long id;

    private String name;

    private Boolean pinned;

    private List<EventShortDto> events;
}
