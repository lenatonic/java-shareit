package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
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
    public Item updateItem(@PathVariable(name = "id") Long idItem,
                           @Valid @RequestBody ItemPatchDto itemPatchDto,
                           @RequestHeader(value = "X-Sharer-User-Id") Long idOwner) {
        itemPatchDto.setId(idItem);
        itemPatchDto.setOwner(idOwner);
        return itemService.updateItem(itemPatchDto);
    }

    @GetMapping("/{id}")
    public ItemDto findItemById(@PathVariable(name = "id") Long id) {
        return itemService.findItemById(id);
    }

    @GetMapping
    public List<ItemDto> findItemsByIdOwner(@RequestHeader(value = "X-Sharer-User-Id") Long idOwner) {
        return itemService.findItemsByIdOwner(idOwner);
    }

    @GetMapping("/search")
    public List<ItemDto> findItemsByText(@RequestParam String text) {
        return itemService.findItemsByText(text);
    }

}