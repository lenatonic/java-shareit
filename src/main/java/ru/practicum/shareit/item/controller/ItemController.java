package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@Valid @RequestBody Item item, @RequestHeader(value = "X-Sharer-User-Id") Long id) {
        item.setOwner(id);
        return itemService.createItem(item);
    }

    @PatchMapping("/{id}")
    public ItemPatchDto updateItem(@PathVariable(name = "id") Long idItem,
                                   @Valid @RequestBody ItemPatchDto itemPatchDto,
                                   @RequestHeader(value = "X-Sharer-User-Id") Long idOwner) {
        itemPatchDto.setId(idItem);
        itemPatchDto.setOwner(idOwner);
        return itemService.updateItem(itemPatchDto);
    }

    @GetMapping("/{id}")
    public ItemOwnerDto findItemById(@RequestHeader(value = "X-Sharer-User-Id") Long idOwner,
                                     @PathVariable(name = "id") Long id) {
        return itemService.findItemById(idOwner, id);
    }

    @GetMapping
    public List<ItemOwnerDto> findItemsByIdOwner(@RequestHeader(value = "X-Sharer-User-Id") Long idOwner) {
        return itemService.findItemsByIdOwner(idOwner);
    }

    @GetMapping("/search")
    public List<ItemDto> findItemsByText(@RequestParam String text) {
        return itemService.findItemsByText(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                 @PathVariable(name = "itemId") Long itemId,
                                 @RequestBody CommentDto commentDto) {
        return itemService.addComment(userId, itemId, commentDto);
    }
}