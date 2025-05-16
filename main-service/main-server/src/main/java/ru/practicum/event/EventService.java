package ru.practicum.event;

import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.NewEventDto;

import java.util.List;
import java.util.Optional;

public interface EventService {

    Optional<EventFullDto> saveEvent(NewEventDto newEventDto, int userId);

    List<EventFullDto> getEvents(String text, List<Integer> categories, boolean paid,
                                 String rangeStart, String rangeEnd, boolean onlyAvailable,
                                 String sort, Integer from, Integer size);

    Optional<EventFullDto> getEventById(int id);
}
