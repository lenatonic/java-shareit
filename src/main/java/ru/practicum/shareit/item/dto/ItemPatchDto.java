package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;

@Data
public class ItemPatchDto {
    Long id;
    String name;
    String description;
    Boolean available;
    ItemRequest request;
    Long owner;
}