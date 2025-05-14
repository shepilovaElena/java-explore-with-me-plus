package ru.practicum.event;

import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.NewEventDto;

public interface EventService {

    EventFullDto saveEvent(NewEventDto newEventDto, int userId);
}
