package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import ru.practicum.category.CategoryService;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.Location;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.user.UserMapper;
import ru.practicum.user.UserRepository;

@RequiredArgsConstructor
public class EventDtoMapper {

    private final CategoryService categoryService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public Event mapToModel(NewEventDto dto, long userId) {
        return Event.builder()
                .initiatorId(userId)
                .annotation(dto.getAnnotation())
                .categoryId(dto.getCategoryId())
                .description(dto.getDescription())
                .eventDate(dto.getEventDate())
                .location_lat(dto.getLocation().getLat())
                .location_lon(dto.getLocation().getLon())
                .paid(dto.getPaid())
                .participantLimit(dto.getParticipantLimit())
                .requestModeration(dto.getRequestModeration())
                .title(dto.getTitle())
                .build();
    }

    public EventShortDto mapToShortDto(Event event) {
        return EventShortDto.builder()
                .annotation(event.getAnnotation())
                .category(categoryService.getById(event.getCategoryId())) // закрыта скобка
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(userMapper.toUserShortDto(
                        userRepository.findById(event.getInitiatorId())
                                .orElseThrow(() -> new RuntimeException("User not found"))
                ))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public EventFullDto mapToFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(categoryService.getById(event.getCategoryId())) // закрыта скобка
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(userMapper.toUserShortDto(
                        userRepository.findById(event.getInitiatorId())
                                .orElseThrow(() -> new RuntimeException("User not found"))
                ))
                .location(Location.builder()
                        .lat(event.getLocation_lat())
                        .lon(event.getLocation_lon())
                        .build())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }
}
