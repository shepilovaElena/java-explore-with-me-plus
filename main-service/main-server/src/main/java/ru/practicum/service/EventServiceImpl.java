package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.error.UserNotFoundException;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.NewEventDto;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    UserService userService = new UserServiceImpl();

    public EventFullDto saveEvent(NewEventDto newEventDto, int userId) {
        if (userService.findUserById(id).isEmpty) {
            throw new UserNotFoundException(id);
        }

    }
}
