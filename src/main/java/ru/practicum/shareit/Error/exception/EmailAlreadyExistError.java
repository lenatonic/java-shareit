package ru.practicum.shareit.Error.exception;

public class EmailAlreadyExistError extends RuntimeException {
    public EmailAlreadyExistError(final String message) {
        super(message);
    }
}