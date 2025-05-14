package ru.practicum.event;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import ru.practicum.error.InvalidEventTimeException;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.NewEventDto;

import java.time.LocalDateTime;
import java.util.List;

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

    @GetMapping("/events")
    public List<EventFullDto> getEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Integer> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return eventService.getEvents(text, categories, paid,
                                        rangeStart, rangeEnd,
                                        onlyAvailable, sort, from, size);
    }
}
