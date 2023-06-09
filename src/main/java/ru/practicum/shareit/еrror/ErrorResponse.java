package ru.practicum.shareit.еrror;

import lombok.Data;

@Data
public class ErrorResponse {
    String error;
    String description;

    public ErrorResponse(String error, String description) {
        this.error = error;
        this.description = description;
    }
}