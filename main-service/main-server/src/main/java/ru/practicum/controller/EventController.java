package ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.practicum.error.InvalidEventTimeException;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.service.EventService;

import java.time.LocalDateTime;

@RequestMapping
@RestController
@RequiredArgsConstructor
public class EventController {
    EventService eventService;

    public EventFullDto saveEvent(@Valid @RequestBody NewEventDto newEventDto,
                                  @RequestHeader(name = "userId") int userId) {
        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new InvalidEventTimeException(newEventDto.getEventDate());
        }
        return eventService.saveEvent(newEventDto, userId);
    }
}
