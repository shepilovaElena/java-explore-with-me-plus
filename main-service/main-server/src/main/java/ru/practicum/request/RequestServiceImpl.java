package ru.practicum.request;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.event.enums.State;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
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
        return requestRepository.findByRequester_Id(userId).stream()
                .map(requestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto addUserRequest(Long userId, Long eventId) {
        User user = findUser(userId);
        Event event = findEvent(eventId);
        requestRepository.findByRequester_IdAndEvent_Id(userId, eventId).ifPresent(
                request -> {
                    throw new ConflictPropertyConstraintException("Нельзя добавить повторный запрос");
                }
        );

        if (event.getInitiatorId() == user.getId()) {
            throw new ConflictRelationsConstraintException(
                    "Инициатор события не может добавить запрос на участие в своём событии"
            );
        }

        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ConflictRelationsConstraintException("Нельзя участвовать в неопубликованном событии");
        }

        Status status = Status.PENDING;
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            status = Status.CONFIRMED;
            addConfirmedRequestToEvent(event);

        } else if (event.getConfirmedRequests() == event.getParticipantLimit()) {
            throw new ConflictPropertyConstraintException("Достигнут лимит запросов на участие");
        }

        Request request = Request.builder()
                .requester(user)
                .status(status)
                .event(event)
                .created(LocalDateTime.now())
                .build();

        return requestMapper.toParticipationRequestDto(requestRepository.save(request));
    }


    @Override
    public ParticipationRequestDto cancelUserRequest(Long userId, Long requestId) {
        User user = findUser(userId);
        Request request = requestRepository.findById(requestId).orElseThrow(
                () -> new NotFoundException("Запрос с id " + requestId + " не найден")
        );

        request.setStatus(Status.CANCELED);

        return requestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    @Override
    public EventRequestStatusUpdateResult changeRequestStatus(Long initiatorId, Long eventId, EventRequestStatusUpdateRequest httpRequest) {
        findUser(initiatorId);
        Event event = findEvent(eventId);
        checkInitiatorEvent(initiatorId, eventId);

        List<Request> requests = requestRepository.findAllByIdIn(httpRequest.getRequestIds());

        if (event.getConfirmedRequests() == event.getParticipantLimit())
            throw new ConflictPropertyConstraintException("Достигнут лимит запросов на участие");

        List<Long> invalidRequestByEventIds = requests.stream()
                .filter(request -> request.getEvent().getId() != eventId)
                .map(Request::getId)
                .toList();

        if (!invalidRequestByEventIds.isEmpty())
            throw new ConflictPropertyConstraintException(
                    "Событие с id " + eventId + " не принадлежит запросам с id " + invalidRequestByEventIds
            );

        List<Long> invalidRequestByStatusIds = requests.stream()
                .filter(request -> request.getStatus() != Status.PENDING)
                .map(Request::getId)
                .toList();
        if (!invalidRequestByStatusIds.isEmpty())
            throw new ConflictPropertyConstraintException("Неверный статус у запросов c id " + invalidRequestByStatusIds);

        if (httpRequest.getStatus().equals(Status.CANCELED)) {
            List<Request> cancelRequests = requests.stream()
                    .peek(request -> request.setStatus(Status.CANCELED))
                    .toList();
            requestRepository.saveAll(cancelRequests);

            List<ParticipationRequestDto> requestDTOs = requests.stream()
                    .map(requestMapper::toParticipationRequestDto)
                    .toList();

            return EventRequestStatusUpdateResult.builder()
                    .confirmedRequests(List.of())
                    .rejectedRequests(requestDTOs)
                    .build();
        }

        long participantLimit = event.getParticipantLimit();
        long confirmedRequests = event.getConfirmedRequests();
        long requestsSize = requests.size();

        if (participantLimit - confirmedRequests < requestsSize)
            throw new ConflictPropertyConstraintException("Достигнут лимит запросов на участие");


        List<Request> confirmedRequestsList = requests.stream()
                .peek(request -> request.setStatus(Status.CONFIRMED))
                .toList();
        requestRepository.saveAll(confirmedRequestsList);
        event.setConfirmedRequests(confirmedRequests + requestsSize);
        eventRepository.save(event);


        List<ParticipationRequestDto> confirmedRequestDTOs = confirmedRequestsList.stream()
                .map(requestMapper::toParticipationRequestDto)
                .toList();

        List<ParticipationRequestDto> canceledRequestDTOs = List.of();

        if (participantLimit - confirmedRequests == requestsSize) {
            List<Request> pendingRequests = requestRepository.findByEvent_IdAndStatus(eventId, Status.PENDING);
            List<Request> cancelRequests = pendingRequests.stream()
                    .peek(request -> request.setStatus(Status.CANCELED))
                    .toList();
            requestRepository.saveAll(cancelRequests);

            canceledRequestDTOs = cancelRequests.stream()
                    .map(requestMapper::toParticipationRequestDto)
                    .toList();
        }

        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(confirmedRequestDTOs)
                .rejectedRequests(canceledRequestDTOs)
                .build();

    }



    @Override
    public List<ParticipationRequestDto> getRequestByInitiator(Long userId, Long eventId) {
        checkInitiatorEvent(userId, eventId);
        return requestRepository.findByEvent_Id(eventId).stream()
                .map(requestMapper::toParticipationRequestDto)
                .toList();
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

    private void addConfirmedRequestToEvent(Event event) {
        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        eventRepository.save(event);
    }

    private void checkInitiatorEvent(Long initiatorId, Long eventId) {
        eventRepository.findByInitiatorIdAndId(initiatorId, eventId).orElseThrow(
                () -> new NotFoundException(
                        "У пользователя с id " + initiatorId + " не найдено событие с id " + eventId)
        );
    }
}
