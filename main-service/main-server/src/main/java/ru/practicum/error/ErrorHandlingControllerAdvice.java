package ru.practicum.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class ErrorHandlingControllerAdvice {

    @ExceptionHandler(InvalidEventTimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadEventTime(InvalidEventTimeException ex) {
        List<String> errorList = List.of(String.format(
                "Ошибка валидации времени начала события %s: событие не может начинаться раньше чем %s",
                ex.getEventDate(), LocalDateTime.now().plusHours(2)
        ));
        return new ErrorResponse(errorList);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFound(UserNotFoundException ex) {
        List<String> errorList = List.of(ex.getMessage());
        return new ErrorResponse(errorList);
    }
}
