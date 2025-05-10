package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.mapper.EndpointHitMapperCustom;
import ru.practicum.model.EndpointHit;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository repository;

    @Override
    public void saveHit(ru.practicum.dto.EndpointHitDto dto) {
        EndpointHit hit = EndpointHitMapperCustom.toModel(dto);
        repository.save(hit);
    }

    @Override
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (unique) {
            return repository.findUniqueHits(start, end, uris);
        } else {
            return repository.findAllHits(start, end, uris);
        }
    }
}
