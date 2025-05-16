package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.StatsClient;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.zaglushkiToDelete.UserService;
import ru.practicum.error.NotFoundException;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.NewEventDto;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final UserService userService;
    private final EventRepository eventRepository;
    private final StatsClient statsClient;

    public Optional<EventFullDto> saveEvent(NewEventDto newEventDto, int userId, String ip) {
        String uri = "/users/" + userId + "/events";
        statsClient.saveHit(EndpointHitDto.builder()
                .app("main-service")
                .ip(ip)
                .uri(uri)
                .timestamp(LocalDateTime.now())
                .build());
        if (userService.findUserById(userId).isEmpty()) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        if (categoryService.findCategoryById(newEventDto.getCategoryId()).isEmpty()) {
            throw new NotFoundException("Категория с id " + newEventDto.getCategoryId() + " не найдена");
        }
        Event event = EventDtoMapper.mapToModel(newEventDto, userId);
        Event savedEvent = eventRepository.save(event);
        return Optional.of(EventDtoMapper.mapToFullDto(savedEvent));
    }

    public List<EventFullDto> getEvents(String text, List<Integer> categories, boolean paid,
                                        String rangeStart, String rangeEnd, boolean onlyAvailable,
                                        String sort, Integer from, Integer size, String ip) {
        String uri = "/events";
        statsClient.saveHit(EndpointHitDto.builder()
                .app("main-service")
                .ip(ip)
                .uri(uri)
                .timestamp(LocalDateTime.now())
                .build());
        Sort sortParam = switch (sort) {
            case "EVENT_DATE" -> Sort.by("eventDate").ascending();
            default -> Sort.unsorted();
        };
        int safeFrom = (from != null) ? from : 0;
        int safeSize = (size != null) ? size : 10;
        PageRequest page = PageRequest.of(safeFrom / safeSize, safeSize, sortParam);
        if (rangeStart == null) {
            rangeStart = LocalDateTime.now().toString();
        }
        List<EventFullDto> events = eventRepository.getEvents(text, categories, paid,
                        rangeStart, rangeEnd, onlyAvailable,
                        page)
                .stream()
                .map(EventDtoMapper::mapToFullDto)
                .toList();
        List<EventFullDto> eventsWithViews = events.stream()
                .map(e -> {
                    String uriEvent = "events/" + e.getId();
                    e.setViews(statsClient.getStats(
                            LocalDateTime.MIN, LocalDateTime.now(), List.of(uriEvent), false)
                            .getFirst().getHits());
                    return e;
                    }
                )
                .toList();
        if (sort.equals("VIEWS")) {
            return events.stream()
                    .sorted(Comparator.comparing(EventFullDto::getViews))
                    .toList();
        }
        return eventsWithViews;
    }

    public Optional<EventFullDto> getEventById(int id, String ip) {
        String uri = "events/" + id;
        statsClient.saveHit(EndpointHitDto.builder()
                .app("main-service")
                .ip(ip)
                .uri(uri)
                .timestamp(LocalDateTime.now())
                .build());
        Optional<Event> event = eventRepository.findById(id);
        if (event.isEmpty()) {
            return Optional.empty();
        }
        EventFullDto dto = EventDtoMapper.mapToFullDto(event.get());
        dto.setViews(statsClient.getStats(
                LocalDateTime.MIN, LocalDateTime.now(), List.of(uri), false)
                .getFirst().getHits());
        return Optional.of(dto);
    }
}
