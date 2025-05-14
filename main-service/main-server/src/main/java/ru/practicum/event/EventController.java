package ru.practicum.event;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import ru.practicum.error.InvalidEventTimeException;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.NewEventDto;

import java.time.LocalDateTime;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class EventController {
    EventService eventService;

    @PostMapping("/users/{userId}/events")
    public EventFullDto saveEvent(@Valid @RequestBody NewEventDto newEventDto,
                                            @PathVariable(name = "userId") int userId) {
        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new InvalidEventTimeException(newEventDto.getEventDate());
        }
        return eventService.saveEvent(newEventDto, userId)
                .orElseThrow(() -> new InternalError("Ошибка при сохранении в БД"));
    }
}
