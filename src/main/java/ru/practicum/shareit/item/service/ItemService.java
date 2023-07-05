package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoCreate;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto createItem(ItemDto item, Long idUser);

    ItemPatchDto updateItem(ItemPatchDto itemPatchDto);

    ItemOwnerDto findItemById(Long idOwner, Long id);

    List<ItemOwnerDto> findItemsByIdOwner(Long idOwner);

    List<ItemDto> findItemsByText(String text);

    CommentDto addComment(Long userId, Long itemId, CommentDto commentDto);
}