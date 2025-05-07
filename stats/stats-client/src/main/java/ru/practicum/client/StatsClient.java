package ru.practicum.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StatsClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${stats-server.url}")
    private String serverUrl;

    public void saveHit(EndpointHitDto dto) {
        HttpEntity<EndpointHitDto> request = new HttpEntity<>(dto, defaultHeaders());
        restTemplate.exchange(serverUrl + "/hit", HttpMethod.POST, request, Void.class);
    }

    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        String url = UriComponentsBuilder.fromHttpUrl(serverUrl + "/stats")
                .queryParam("start", start)
                .queryParam("end", end)
                .queryParam("unique", unique)
                .queryParam("uris", uris != null ? String.join(",", uris) : null)
                .toUriString();

        ResponseEntity<ViewStatsDto[]> response = restTemplate.exchange(url, HttpMethod.GET,
                new HttpEntity<>(defaultHeaders()), ViewStatsDto[].class);

        return Arrays.asList(response.getBody());
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
