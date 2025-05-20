package ru.practicum.error;

public class ConditionsNotMetException extends RuntimeException {

    public ConditionsNotMetException(String text) {
        super(text);
    }
}
