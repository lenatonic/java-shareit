package ru.practicum.shareit.error.exception;

public class AccessErrorException extends RuntimeException {
    public AccessErrorException(final String message) {
        super(message);
    }
}