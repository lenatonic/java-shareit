package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@Valid @RequestBody Item item,
                                             @Positive @RequestHeader(value = "X-Sharer-User-Id") Long id) {
        return itemClient.createItem(item, id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateItem(@Positive @PathVariable(name = "id") Long idItem,
                                             @RequestBody ItemPatchDto itemPatchDto,
                                             @Positive @RequestHeader(value = "X-Sharer-User-Id") Long idOwner) {
        return itemClient.updateItem(itemPatchDto, idItem, idOwner);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findItemById(@Positive @RequestHeader(value = "X-Sharer-User-Id") Long idOwner,
                                     @Positive @PathVariable(name = "id") Long id) {
        return itemClient.findItemById(idOwner, id);
    }

    @GetMapping
    public ResponseEntity<Object> findItemsByIdOwner(@Positive @RequestHeader(value = "X-Sharer-User-Id") Long idOwner,
                                                 @RequestParam(name = "from", defaultValue = "0")
                                                 @Positive Integer from,
                                                 @RequestParam(name = "size", defaultValue = "10")
                                                 @Positive Integer size) {
        return itemClient.findItemsByIdOwner(idOwner, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> findItemsByText(@RequestParam String text,
                                         @RequestParam(name = "from", defaultValue = "0")
                                         @Positive Integer from,
                                         @RequestParam(name = "size", defaultValue = "10")
                                         @Positive Integer size) {
        return itemClient.findItemsByText(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@NotNull @RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                 @Positive @PathVariable(name = "itemId") Long itemId,
                                 @RequestBody CommentDto commentDto) {
        return itemClient.addComment(userId, itemId, commentDto);
    }
}

