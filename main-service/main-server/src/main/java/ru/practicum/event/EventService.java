package ru.practicum.event;

import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdatedEventDto;

import java.util.List;
import java.util.Optional;

public interface EventService {

    Optional<EventFullDto> saveEvent(NewEventDto newEventDto, int userId, String ip);

    List<EventFullDto> getEvents(String text, List<Integer> categories, boolean paid,
                                 String rangeStart, String rangeEnd, boolean onlyAvailable,
                                 String sort, Integer from, Integer size,
                                 String ip, String user);

    Optional<EventFullDto> getEventById(int id, String ip);

    List<EventShortDto> getEventsByUserId(int userId, Integer from, Integer size, String ip);

    Optional<EventShortDto> getEventByUserIdAndEventId(int userId, int eventId,
                                                       Integer from, Integer size, String ip);

    Optional<EventFullDto> updateEvent(UpdatedEventDto updatedEventDto,
                                       int userId, int eventId, String ip);

    Optional<EventFullDto> updateAdminEvent(UpdatedEventDto updatedEventDto,
                                            int eventId, String ip);
}