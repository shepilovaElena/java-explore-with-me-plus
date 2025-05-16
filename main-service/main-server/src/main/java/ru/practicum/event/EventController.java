package ru.practicum.event;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import ru.practicum.error.InvalidEventTimeException;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.error.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping("/users/{userId}/events")
    public EventFullDto saveEvent(@Valid @RequestBody NewEventDto newEventDto,
                                  @PathVariable(name = "userId") int userId,
                                  HttpServletRequest request) {
        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new InvalidEventTimeException(newEventDto.getEventDate());
        }
        String ip = request.getRemoteAddr();
        return eventService.saveEvent(newEventDto, userId, ip)
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
            @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request
    ) {
        String ip = request.getRemoteAddr();
        return eventService.getEvents(text, categories, paid,
                                        rangeStart, rangeEnd,
                                        onlyAvailable, sort, from, size,
                                        ip, "user");
    }

    @GetMapping("/admin/events")
    public List<EventFullDto> getAdminEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Integer> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request
    ) {
        String ip = request.getRemoteAddr();
        return eventService.getEvents(text, categories, paid,
                rangeStart, rangeEnd,
                onlyAvailable, sort, from, size,
                ip, "admin");
    }

    @GetMapping("/events/{id}")
    public EventFullDto getEventById(@PathVariable int id, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        return eventService.getEventById(id, ip)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + id + " не найден"));
    }
}
