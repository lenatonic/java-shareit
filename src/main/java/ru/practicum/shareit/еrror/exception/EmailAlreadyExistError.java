package ru.practicum.shareit.еrror.exception;

public class EmailAlreadyExistError extends RuntimeException {
    public EmailAlreadyExistError(final String message) {
        super(message);
    }
}