package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookItemRequestDto {
    private long itemId;
    @FutureOrPresent(message = "Время начала бронирования не может быть в прошлом")
    private LocalDateTime start;

    @Future(message = "Время завершения бронирования не может быть в прошлом")
    private LocalDateTime end;
}