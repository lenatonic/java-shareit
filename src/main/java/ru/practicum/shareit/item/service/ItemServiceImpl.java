package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.Error.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserService userService;

    @Override
    public Item createItem(Item item) {
        if (userService.isUserExistById(item.getOwner())) {
            log.debug("Пользователь с id = {}, создаёт предмет с name = {}", item.getOwner(), item.getName());
            return itemStorage.createItem(item);
        }
        throw new NotFoundException("Пользователя с id = " + item.getOwner() + " не существует.");
    }

    @Override
    public Item updateItem(ItemPatchDto itemPatchDto) {
        if (!itemStorage.isItemExist(itemPatchDto.getId())) {
            throw new NotFoundException("Предмета с id = " + itemPatchDto.getId() + " не существует.");
        }
        if (itemStorage.isOwner(itemPatchDto.getId(), itemPatchDto.getOwner())) {
            log.debug("Пользователь с id = {}, создаёт предмет с name = {}", itemPatchDto.getOwner(),
                    itemPatchDto.getName());
            return itemStorage.updateItem(itemPatchDto);
        }
        throw new NotFoundException("Пользователя с id = " + itemPatchDto.getOwner() + " не существует.");
    }

    @Override
    public boolean isItemExist(Long id) {
        return itemStorage.isItemExist(id);
    }

    @Override
    public Item findItemById(Long id) {
        isItemExist(id);
        log.debug("Находим предмет по id = {}.", id);
        return itemStorage.findItemById(id);
    }

    @Override
    public List<Item> findItemsByIdOwner(Long idOwner) {
        if (userService.isUserExistById(idOwner)) {
            log.debug("Вывод списка предметов владельца с id = {}.", idOwner);
            return itemStorage.findItemsByIdOwner(idOwner);
        } else {
            throw new NotFoundException("Пользователя с id = " + idOwner + " не существует.");
        }
    }

    @Override
    public List<Item> findItemsByText(String text) {
        return itemStorage.findItemsByText(text);
    }
}