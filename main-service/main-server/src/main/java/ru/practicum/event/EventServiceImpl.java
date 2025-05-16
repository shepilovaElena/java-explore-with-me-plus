package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.zaglushkiToDelete.UserService;
import ru.practicum.error.NotFoundException;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.NewEventDto;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final UserService userService;
    private final EventRepository eventRepository;

    public Optional<EventFullDto> saveEvent(NewEventDto newEventDto, int userId) {
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
                                        String sort, Integer from, Integer size) {
        Sort sortParam = switch (sort) {
            case "EVENT_DATE" -> Sort.by("eventDate").ascending();
            //=case "VIEWS" -> Sort.by("views").descending(); // <--------- мы же не храним VIEWS в БД
            default -> Sort.unsorted();
        };
        int safeFrom = (from != null) ? from : 0;
        int safeSize = (size != null) ? size : 10;
        PageRequest page = PageRequest.of(safeFrom / safeSize, safeSize, sortParam);
        return eventRepository.getEvents(text, categories, paid,
                        rangeStart, rangeEnd, onlyAvailable,
                        page)
                .stream()
                .map(EventDtoMapper::mapToFullDto)
                .toList();
    }

    public Optional<EventFullDto> getEventById(int id) {
        Optional<Event> event = eventRepository.findById(id);
        if (event.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(
                EventDtoMapper.mapToFullDto(event.get())
        );
    }
}
