package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@AllArgsConstructor
public class ItemDto {
    private Long id;

    @NotBlank(message = "Название не может быть пустым")
    @Size(min = 1, max = 30)
    private String name;

    @NotBlank(message = "Описание не может быть пустым")
    @Size(min = 1, max = 30)
    private String description;

    @NotNull(message = "Поле available не может быть пустым")
    private Boolean available;

    private Long ownerId;

    private Long requestId;
}