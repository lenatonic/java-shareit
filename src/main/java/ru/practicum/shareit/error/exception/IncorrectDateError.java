package ru.practicum.shareit.error.exception;

public class IncorrectDateError extends RuntimeException {
    public IncorrectDateError(final String message) {
        super(message);
    }
}
