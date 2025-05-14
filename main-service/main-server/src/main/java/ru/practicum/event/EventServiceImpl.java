package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.zaglushkiToDelete.UserService;
import ru.practicum.error.NotFoundException;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.NewEventDto;

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
}
