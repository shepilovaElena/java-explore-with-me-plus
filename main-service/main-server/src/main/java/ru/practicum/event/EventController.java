package ru.practicum.event;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.practicum.error.InvalidEventTimeException;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.NewEventDto;

import java.time.LocalDateTime;
import java.util.Optional;

@RequestMapping
@RestController
@RequiredArgsConstructor
public class EventController {
    EventService eventService;

    public Optional<EventFullDto> saveEvent(@Valid @RequestBody NewEventDto newEventDto,
                                            @RequestHeader(name = "userId") int userId) {
        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new InvalidEventTimeException(newEventDto.getEventDate());
        }
        return eventService.saveEvent(newEventDto, userId)
                .orElseThrow(() -> new InternalError("Ошибка при сохранении в БД"));
    }
}
