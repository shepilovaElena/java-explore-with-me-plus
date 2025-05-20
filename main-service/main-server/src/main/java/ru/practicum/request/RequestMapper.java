package ru.practicum.request;


import org.mapstruct.Mapper;
import ru.practicum.dto.request.ParticipationRequestDto;

@Mapper(componentModel = "spring")
public interface RequestMapper {

    ParticipationRequestDto toParticipationRequestDto(Request request);
}
