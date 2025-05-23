package ru.practicum.service;

import lombok.RequiredArgsConstructor;
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
public class StatsServiceImpl implements StatsService {

    private final StatsRepository repository;

    @Override
    public void saveHit(EndpointHitDto dto) {
        EndpointHit hit = EndpointHitMapperCustom.toModel(dto);
        repository.save(hit);
    }

    @Override
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (uris != null && uris.isEmpty()) {
            uris = null;
        }

        List<ViewStatsDto> result = unique ?
                repository.findUniqueHits(start, end, uris) :
                repository.findAllHits(start, end, uris);

        System.out.println("Stats found: " + result.size());
        result.forEach(System.out::println);
        return result;
    }
}
