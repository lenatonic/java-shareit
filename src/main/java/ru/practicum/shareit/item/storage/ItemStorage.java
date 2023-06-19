package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item createItem(Item item);

    Item updateItem(ItemPatchDto itemPatchDto);

    boolean isItemExist(Long id);

    boolean isOwner(Long idItem, Long idOwner);

    Item findItemById(Long id);

    List<Item> findItemsByIdOwner(Long idOwner);

    List<Item> findItemsByText(String text);
}