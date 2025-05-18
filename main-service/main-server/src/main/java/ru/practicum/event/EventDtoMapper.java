package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.Location;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.user.UserService;

@RequiredArgsConstructor
public class EventDtoMapper {
    static CategoryService categoryService;
    static UserService userService;

    public static Event mapToModel(NewEventDto dto, int userId) {
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

    public static EventShortDto mapToShortDto(Event event) {
        return EventShortDto.builder()
                .annotation(event.getAnnotation())
                .category(categoryService.findById(event.getCategoryId()).get())
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserDtoMapper.mapToShortDto(
                        userService.findUserById(
                                event.getInitiatorId()).get()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static EventFullDto mapToFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(categoryService.findById(event.getCategoryId()).get())
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(UserDtoMapper.mapToShortDto(
                        userService.findUserById(
                                event.getInitiatorId()).get()))
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
