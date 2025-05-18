package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.StatsClient;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.UpdatedEventDto;
import ru.practicum.dto.event.enums.State;
import ru.practicum.dto.event.enums.StateAction;
import ru.practicum.error.BadRequestException;
import ru.practicum.error.ConditionsNotMetException;
import ru.practicum.error.NotFoundException;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.NewEventDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final UserService userService;
    private final EventRepository eventRepository;
    private final StatsClient statsClient;
    private final CategoryService categoryService;

    public Optional<EventFullDto> saveEvent(NewEventDto newEventDto, int userId, String ip) {
        if (userService.findById(userId).isEmpty()) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        if (categoryService.findById(newEventDto.getCategoryId()).isEmpty()) {
            throw new NotFoundException("Категория с id " + newEventDto.getCategoryId() + " не найдена");
        }
        String uri = "/users/" + userId + "/events";
        statsClient.saveHit(EndpointHitDto.builder()
                .app("main-service")
                .ip(ip)
                .uri(uri)
                .timestamp(LocalDateTime.now())
                .build());
        Event event = EventDtoMapper.mapToModel(newEventDto, userId);
        Event savedEvent = eventRepository.save(event);
        return Optional.of(EventDtoMapper.mapToFullDto(savedEvent));
    }

    public Optional<EventFullDto> updateEvent(UpdatedEventDto updatedEvent,
                                              int userId, int eventId, String ip) {
        if (userService.findById(userId).isEmpty()) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        Optional<Event> eventOpt = eventRepository.findById(eventId);
        if (eventOpt.isEmpty()) {
            throw new NotFoundException("Событие с id " + eventId + " не найдено");
        }
        String uri = "/users/" + userId + "/events/" + eventId;
        statsClient.saveHit(EndpointHitDto.builder()
                .app("main-service")
                .ip(ip)
                .uri(uri)
                .timestamp(LocalDateTime.now())
                .build());

        Event event = eventOpt.get();
        if (!event.getState().equals(State.CANCELED) && !event.getRequestModeration()) {
            throw new ConditionsNotMetException(
                    "Изменить можно только отменённое событие или находящееся на модерации");
        }
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ConditionsNotMetException(
                    "Нельзя изменить событие, которое начинается в течение двух часов");
        }

        applyUpdate(event, updatedEvent);
        Event savedUpdatedEvent = eventRepository.save(event);

        return Optional.of(EventDtoMapper.mapToFullDto(savedUpdatedEvent));
    }

    public Optional<EventFullDto> updateAdminEvent(UpdatedEventDto updatedEvent,
                                                   int eventId, String ip) {
        Optional<Event> eventOpt = eventRepository.findById(eventId);
        if (eventOpt.isEmpty()) {
            throw new NotFoundException("Событие с id " + eventId + " не найдено");
        }
        String uri = "/admin/events/" + eventId;
        statsClient.saveHit(EndpointHitDto.builder()
                .app("main-service")
                .ip(ip)
                .uri(uri)
                .timestamp(LocalDateTime.now())
                .build());

        Event event = eventOpt.get();
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new ConditionsNotMetException(
                    "Нельзя изменить событие, которое начинается в течение часа");
        }
        if (updatedEvent.getStateAction() != null
                && updatedEvent.getStateAction().equals(StateAction.PUBLISH_EVENT)
                && !event.getRequestModeration()) {
            throw new ConditionsNotMetException(
                    "Нельзя опубликовать событие, которое не находится в состоянии модерации");
        }
        if (updatedEvent.getStateAction() != null
                && updatedEvent.getStateAction().equals(StateAction.REJECT_EVENT)
                && event.getState().equals(State.PUBLISHED)) {
            throw new ConditionsNotMetException(
                    "Нельзя отклонить опубликованное событие");
        }

        applyUpdate(event, updatedEvent);
        Event savedUpdatedEvent = eventRepository.save(event);

        return Optional.of(EventDtoMapper.mapToFullDto(savedUpdatedEvent));
    }

    public List<EventFullDto> getEvents(String text, List<Integer> categories, boolean paid,
                                        String rangeStart, String rangeEnd, boolean onlyAvailable,
                                        String sort, Integer from, Integer size, String ip, String user) {
        boolean isAdmin = !"user".equalsIgnoreCase(user);
        String uri = isAdmin ? "/admin/events" : "/events";
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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(rangeStart, formatter);
        LocalDateTime end = (rangeEnd == null || rangeEnd.isBlank())
                ? null
                : LocalDateTime.parse(rangeEnd, formatter);

        List<EventFullDto> events = eventRepository.getEvents(text, categories, paid,
                        start, end, onlyAvailable,
                        isAdmin, page)
                .stream()
                .map(EventDtoMapper::mapToFullDto)
                .toList();

        List<EventFullDto> eventsWithViews = events.stream()
                .map(e -> {
                    String uriEvent = "events/" + e.getId();
                    List<ViewStatsDto> statsList = statsClient.getStats(
                                    LocalDateTime.MIN, LocalDateTime.now(), List.of(uriEvent), false);
                    long views = statsList.isEmpty() ? 0L : statsList.getFirst().getHits();
                    e.setViews(views);
                    return e;
                    }
                ).toList();

        if (sort.equals("VIEWS")) {
            return events.stream()
                    .sorted(Comparator.comparing(EventFullDto::getViews))
                    .toList();
        }

        return eventsWithViews;
    }

    public List<EventShortDto> getEventsByUserId(int userId, Integer from, Integer size, String ip) {
        if (userService.findById(userId).isEmpty()) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        if (size != null & size < 0) {
            throw new BadRequestException("Некорректный запрос: размер возвращаемого списка отрицательный");
        }
        String uri = "/users/" + userId + "/events";
        statsClient.saveHit(EndpointHitDto.builder()
                .app("main-service")
                .ip(ip)
                .uri(uri)
                .timestamp(LocalDateTime.now())
                .build());


        int safeFrom = (from != null) ? from : 0;
        int safeSize = (size != null) ? size : 10;
        PageRequest page = PageRequest.of(safeFrom / safeSize, safeSize);
        List<Event> userEvents = eventRepository.findAllByUserId(userId, page);

        return userEvents.stream()
                .map(e -> {
                            String uriEvent = "events/" + e.getId();
                            List<ViewStatsDto> statsList = statsClient.getStats(
                                    LocalDateTime.MIN, LocalDateTime.now(), List.of(uriEvent), false);
                            long views = statsList.isEmpty() ? 0L : statsList.getFirst().getHits();
                            e.setViews(views);
                            return e;
                        }
                )
                .map(EventDtoMapper::mapToShortDto)
                .toList();
    }

    public Optional<EventShortDto> getEventByUserIdAndEventId(int userId, int eventId,
                                                              Integer from, Integer size, String ip) {
        if (userService.findById(userId).isEmpty()) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        String uri = "/users/" + userId + "/events/" + eventId;
        statsClient.saveHit(EndpointHitDto.builder()
                .app("main-service")
                .ip(ip)
                .uri(uri)
                .timestamp(LocalDateTime.now())
                .build());

        Optional<Event> event = eventRepository.findByUserIdAndId(userId, eventId);
        if (event.isEmpty()) {
            return Optional.empty();
        }
        EventShortDto eventShortDto = EventDtoMapper.mapToShortDto(event.get());

        return Optional.of(eventShortDto);
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
        List<ViewStatsDto> statsList = statsClient.getStats(
                LocalDateTime.MIN, LocalDateTime.now(), List.of(uri), false);
        long views = statsList.isEmpty() ? 0L : statsList.getFirst().getHits();
        dto.setViews(views);
        return Optional.of(dto);
    }

    public void applyUpdate(Event event, UpdatedEventDto dto) {
        if (dto.getAnnotation() != null) {
            event.setAnnotation(dto.getAnnotation());
        }
        if (dto.getDescription() != null) {
            event.setDescription(dto.getDescription());
        }
        if (dto.getEventDate() != null) {
            event.setEventDate(dto.getEventDate());
        }
        if (dto.getLocation() != null) {
            event.setLocation_lat(dto.getLocation().getLat());
            event.setLocation_lon(dto.getLocation().getLon());
        }
        if (dto.getPaid() != null) {
            event.setPaid(dto.getPaid());
        }
        if (dto.getParticipantLimit() > 0) {
            event.setParticipantLimit(dto.getParticipantLimit());
        }
        if (dto.getRequestModeration() != null) {
            event.setRequestModeration(dto.getRequestModeration());
        }
        if (dto.getTitle() != null) {
            event.setTitle(dto.getTitle());
        }
        if (dto.getCategoryId() > 0) {
            categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Категория не найдена"));
            event.setCategoryId(dto.getCategoryId());
        }
        if (dto.getStateAction() != null) {
            switch (dto.getStateAction()) {
                case SEND_TO_REVIEW -> event.setState(State.PENDING);
                case CANCEL_REVIEW -> event.setState(State.CANCELED);
                case PUBLISH_EVENT -> event.setState(State.PUBLISHED);
                case REJECT_EVENT -> event.setState(State.CANCELED);
                default -> throw new BadRequestException("Неизвестное действие: " + dto.getStateAction());
            }
        }
    }
}
