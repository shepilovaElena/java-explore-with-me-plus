package ru.practicum.event;

import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.NewEventDto;

import java.util.Optional;

public interface EventService {

    Optional<EventFullDto> saveEvent(NewEventDto newEventDto, int userId);
}
