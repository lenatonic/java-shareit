package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item createItem(Item item);

    Item updateItem(ItemPatchDto itemPatchDto);

    boolean isItemExist(Long id);

    Item findItemById(Long id);

    List<Item> findItemsByIdOwner(Long idOwner);

    List<Item> findItemsByText(String text);
}