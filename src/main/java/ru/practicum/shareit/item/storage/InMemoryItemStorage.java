package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class InMemoryItemStorage implements ItemStorage {
    private final HashMap<Long, Item> items = new HashMap<>();
    private Long id = 0L;

    @Override
    public Item createItem(Item item) {
        item.setId(++id);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItem(ItemPatchDto itemPatchDto) {
        Item updateItem = items.get(itemPatchDto.getId());
        if (itemPatchDto.getName() != null) {
            updateItem.setName(itemPatchDto.getName());
        }
        if (itemPatchDto.getAvailable() != null) {
            updateItem.setAvailable(itemPatchDto.getAvailable());
        }
        if (itemPatchDto.getDescription() != null) {
            updateItem.setDescription(itemPatchDto.getDescription());
        }
        return updateItem;
    }

    @Override
    public boolean isItemExist(Long id) {
        if (items.containsKey(id)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isOwner(Long idItem, Long idOwner) {
        if (items.get(idItem).getOwner().equals(idOwner)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Item findItemById(Long id) {
        return items.get(id);
    }

    @Override
    public List<Item> findItemsByIdOwner(Long idOwner) {
        List<Item> userItems = new ArrayList<>();
        items.values().stream().filter(a -> a.getOwner().equals(idOwner)).forEach(userItems::add);
        return userItems;
    }

    @Override
    public List<Item> findItemsByText(String text) {
        List<Item> itemsByText = new ArrayList<>();
        if (text.isEmpty()) {
            return itemsByText;
        } else {
            items.values().stream()
                    .filter(a -> (a.getName().toLowerCase().contains(text.toLowerCase()) ||
                            a.getDescription().toLowerCase().contains(text.toLowerCase())) &&
                            a.getAvailable())
                    .forEach(itemsByText::add);
        }
        return itemsByText;
    }
}