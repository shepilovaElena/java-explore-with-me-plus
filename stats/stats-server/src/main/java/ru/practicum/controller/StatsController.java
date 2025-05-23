package ru.practicum.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
public class StatsController {

    private final StatsService service;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveHit(@RequestBody @Valid EndpointHitDto hitDto) {
        service.saveHit(hitDto);
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> getStats(@NotNull
                                       @RequestParam("start")
                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                       LocalDateTime start,
                                       @NotNull
                                       @RequestParam("end")
                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                       LocalDateTime end,
                                       @RequestParam(value = "uris", required = false)
                                       List<String> uris,
                                       @RequestParam(value = "unique", defaultValue = "false")
                                       boolean unique) throws Exception {
        if (start.isAfter(end) || start.equals(end)) {
            throw new Exception("The start must come before the end.");
        }
        return service.getStats(start, end, uris, unique);
    }
}
