package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.mapper.EndpointHitMapperCustom;
import ru.practicum.model.EndpointHit;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {

    private final StatsRepository repository;

    @Override
    public void saveHit(EndpointHitDto dto) {
        log.debug("Saving hit: {}", dto);
        EndpointHit hit = EndpointHitMapperCustom.toModel(dto);
        repository.save(hit);
        log.debug("Hit saved successfully.");
    }

    @Override
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        log.debug("Fetching stats: start={}, end={}, uris={}, unique={}", start, end, uris, unique);

        if (uris != null && uris.isEmpty()) {
            uris = null;
        }

        List<ViewStatsDto> result = unique ?
                repository.findUniqueHits(start, end, uris) :
                repository.findAllHits(start, end, uris);

        log.debug("Stats result count: {}", result.size());
        result.forEach(stat -> log.trace("Stat record: {}", stat));

        return result;
    }
}
