package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

@Setter
@Getter
@Builder
public class ItemDtoCreate {
    Long id;
    String name;
    String description;
    User owner;
    Boolean available;
    ItemRequest itemRequest;
}
