package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private Long id;

    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    @NotBlank(message = "Описание должно быть заполнено")
    private String description;

    @NotNull(message = "Поле доступности предмета не проставлено")
    private Boolean available;

    private Long owner;

    private Long requestId;
}