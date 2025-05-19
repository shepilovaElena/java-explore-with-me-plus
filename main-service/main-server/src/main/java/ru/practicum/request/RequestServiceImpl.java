package ru.practicum.request;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.event.enums.State;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.dto.request.enums.Status;
import ru.practicum.event.Event;
import ru.practicum.event.EventRepository;
import ru.practicum.exception.ConflictPropertyConstraintException;
import ru.practicum.exception.ConflictRelationsConstraintException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final RequestMapper requestMapper;
    private final EventRepository eventRepository;

    @Override
    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        findUser(userId);
        return  requestRepository.findByRequester_Id(userId).stream()
                .map(requestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto addUserRequest(Long userId, Long eventId) {
        User user = findUser(userId);
        Event event = findEvent(eventId);
        requestRepository.findByRequester_IdAndEvent_Id(userId, eventId).ifPresent(
                request -> {throw  new ConflictPropertyConstraintException("Нельзя добавить повторный запрос");
                }
        );

        if (event.getInitiatorId() == user.getId()) {
            throw new ConflictRelationsConstraintException(
                    "Инициатор события не может добавить запрос на участие в своём событии"
            );
        }

        if(!event.getState().equals(State.PUBLISHED)) {
            throw new ConflictRelationsConstraintException("Нельзя участвовать в неопубликованном событии");
        }

        long limit = event.getParticipantLimit();
        if ( limit > 0) {
            long requestCount = requestRepository.findByEvent_Id(eventId).stream()
                    .toList()
                    .size();
            if (requestCount >= limit) {
                throw new ConflictPropertyConstraintException("Достигнут лимит запросов на участие");
            }
        }

        Request request = Request.builder()
                .requester(user)
                .status(Status.PENDING)
                .event(event)
                .created(LocalDateTime.now())
                .build();

        return requestMapper.toDto(requestRepository.save(request));
    }


    @Override
    public ParticipationRequestDto cancelUserRequest(Long userId, Long requestId) {
        User user = findUser(userId);
        Request request = requestRepository.findById(requestId).orElseThrow(
                () -> new NotFoundException("Запрос с id " + requestId + " не найден")
        );

        request.setStatus(Status.CANCELED);

        return requestMapper.toDto(requestRepository.save(request));
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id " + userId + " не найден")
        );
    }

    private Event findEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Событие  с id " + eventId + " не найдено")
        );
    }
}
