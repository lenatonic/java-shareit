package ru.practicum.shareit.error.exception;

public class EmailAlreadyExistError extends RuntimeException {
    public EmailAlreadyExistError(final String message) {
        super(message);
    }
}