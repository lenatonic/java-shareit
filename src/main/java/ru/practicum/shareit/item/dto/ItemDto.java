package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.model.ItemRequest;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class ItemDto {
    Long id;
    String name;
    String description;
    Boolean available;
    Long requestId;
}