package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto createItem(Item item);

    Item updateItem(ItemPatchDto itemPatchDto);

    boolean isItemExist(Long id);

    ItemDto findItemById(Long id);

    List<ItemDto> findItemsByIdOwner(Long idOwner);

    List<ItemDto> findItemsByText(String text);
}