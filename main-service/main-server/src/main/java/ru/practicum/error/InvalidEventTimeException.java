package ru.practicum.error;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class InvalidEventTimeException extends RuntimeException {
    private final LocalDateTime eventDate;

    public InvalidEventTimeException(LocalDateTime eventDate) {
        super("Дата события невалидна: " + eventDate);
        this.eventDate = eventDate;
    }

}
