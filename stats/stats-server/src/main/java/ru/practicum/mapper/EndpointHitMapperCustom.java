package ru.practicum.mapper;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.model.EndpointHit;

public class EndpointHitMapperCustom {
    public static EndpointHit toModel(EndpointHitDto endpointHitDto) {
        return EndpointHit.builder()
                .ip(endpointHitDto.getIp())
                .uri(endpointHitDto.getUri())
                .app(endpointHitDto.getApp())
                .timepoint(endpointHitDto.getTimestamp())
                .build();
    }

    public static EndpointHitDto toDto(EndpointHit endpointHit) {
        return EndpointHitDto.builder()
                .ip(endpointHit.getIp())
                .uri(endpointHit.getUri())
                .app(endpointHit.getApp())
                .timestamp(endpointHit.getTimepoint()).build();
    }
}
