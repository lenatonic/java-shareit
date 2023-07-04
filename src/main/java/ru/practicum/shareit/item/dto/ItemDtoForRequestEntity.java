package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.request.model.ItemRequest;

@Setter
@Getter
@Builder
public class ItemDtoForRequestEntity {
    Long id;
    String name;
    String description;
    Boolean available;
    ItemRequest request;
}
