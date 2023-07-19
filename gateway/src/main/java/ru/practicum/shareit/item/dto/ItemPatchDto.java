package ru.practicum.shareit.item.dto;

import lombok.Getter;
import ru.practicum.shareit.request.dto.ItemRequest;

@Getter
public class ItemPatchDto {
    Long id;
    String name;
    String description;
    Boolean available;
    ItemRequest request;
    Long owner;
}