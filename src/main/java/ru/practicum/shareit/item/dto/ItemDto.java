package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;

/**
 * TODO Sprint add-controllers.
 */
@Data
@RequiredArgsConstructor
public class ItemDto {
    Long id;
    String name;
    String description;
    Boolean available;
    ItemRequest request;
}