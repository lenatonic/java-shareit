package ru.practicum.shareit.request.dto;

import lombok.Getter;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ItemRequestDto {

    @Positive
    private Long id;

    @NotBlank
    private String description;

    @NotNull
    private User requestor;

    @NotNull
    private LocalDateTime created;

    private List<ItemDto> items;
}