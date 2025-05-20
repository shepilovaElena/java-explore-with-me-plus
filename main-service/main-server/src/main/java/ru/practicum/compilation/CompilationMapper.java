package ru.practicum.compilation;

import org.mapstruct.Mapper;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.event.EventShortDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompilationMapper {

    CompilationDto toDto(Compilation compilation, List<EventShortDto> events);
}
