package ru.practicum.shareit.item.dto;

import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Setter
public class CommentDto {
    private Long id;

    @NotEmpty(message = "Описание не может быть пустым")
    private String text;

    private String authorName;
    private LocalDateTime created;
}