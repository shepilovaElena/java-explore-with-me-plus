package ru.practicum.event;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdatedEventDto;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping("/users/{userId}/events")
    public EventFullDto saveEvent(@Valid @RequestBody NewEventDto newEventDto,
                                  @PathVariable(name = "userId") Long userId,
                                  HttpServletRequest request) {
//        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
//            throw new InvalidEventTimeException(newEventDto.getEventDate());
//        }
        String ip = request.getRemoteAddr();
        return eventService.saveEvent(newEventDto, userId, ip);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventFullDto updateEventByIdAndUserId(@RequestBody @Valid UpdatedEventDto updatedEventDto,
                                                 @PathVariable Long userId,
                                                 @PathVariable Long eventId,
                                                 HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        return eventService.updateEvent(updatedEventDto, userId, eventId, ip);
    }

    @PatchMapping("/admin/events/{eventId}")
    public EventFullDto updateAdminEventByIdAndUserId(@RequestBody @Valid UpdatedEventDto updatedEventDto,
                                                 @PathVariable Long eventId,
                                                 HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        return eventService.updateAdminEvent(updatedEventDto, eventId, ip);
    }

    @GetMapping("/events/{id}")
    public EventFullDto getEventById(@PathVariable Long id, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        return eventService.getEventById(id, ip);
    }

    @GetMapping("/events")
    public List<EventFullDto> getEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
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

    @GetMapping("/users/{userId}/events")
    public List<EventShortDto> getEventsByUserId(@PathVariable Long userId, HttpServletRequest request,
                                                 @RequestParam(required = false) int from,
                                                 @RequestParam(required = false) int size) {
        String ip = request.getRemoteAddr();
        return eventService.getEventsByUserId(userId, from, size, ip);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventShortDto getEventByUserIdAndEventId(@PathVariable Long userId,
                                                          @PathVariable Long eventId,
                                                          HttpServletRequest request,
                                                          @RequestParam(required = false) int from,
                                                          @RequestParam(required = false) int size) {
        String ip = request.getRemoteAddr();
        return eventService.getEventByUserIdAndEventId(userId, eventId, from, size, ip);
    }

    @GetMapping("/admin/events")
    public List<EventFullDto> getAdminEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
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
}