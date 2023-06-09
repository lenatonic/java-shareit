package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.mapper.ItemMap;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.еrror.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserService userService;
    private final ItemMap itemMap;

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
        Item itemById = itemStorage.findItemById(itemPatchDto.getId());
        if (itemById == null) {
            throw new NotFoundException("Предмета с id = " + itemPatchDto.getId() + " не существует.");
        }
        if (itemById.getOwner().equals(itemPatchDto.getOwner())) {
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
    public ItemDto findItemById(Long id) {
        isItemExist(id);
        log.debug("Находим предмет по id = {}.", id);
        return itemMap.toItemDto(itemStorage.findItemById(id));
    }

    @Override
    public List<ItemDto> findItemsByIdOwner(Long idOwner) {
        List<ItemDto> itemsDto = new ArrayList<>();
        if (userService.isUserExistById(idOwner)) {
            log.debug("Вывод списка предметов владельца с id = {}.", idOwner);
            itemStorage.findItemsByIdOwner(idOwner)
                    .stream()
                    .forEach(a -> itemsDto.add(itemMap.toItemDto(a)));
        } else {
            throw new NotFoundException("Пользователя с id = " + idOwner + " не существует.");
        }
        return itemsDto;
    }

    @Override
    public List<ItemDto> findItemsByText(String text) {
        List<ItemDto> itemsDto = new ArrayList<>();
        itemStorage.findItemsByText(text)
                .stream()
                .forEach(a -> itemsDto.add(itemMap.toItemDto(a)));
        return itemsDto;
    }
}