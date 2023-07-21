package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.dto.ItemRequest;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemPatchDto {
    Long id;
    String name;
    String description;
    Boolean available;
    ItemRequest request;
    Long owner;
}