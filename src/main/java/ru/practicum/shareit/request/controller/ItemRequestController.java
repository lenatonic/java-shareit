package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    ItemRequestDto addRequest(@RequestHeader(value = "X-Sharer-User-Id") Long idRequestor,
                              @RequestBody @Valid ItemRequestDto requestDto) {
        return itemRequestService.addRequest(requestDto, idRequestor, LocalDateTime.now());
    }

    @GetMapping
    List<ItemRequestDto> findAllRequests(@RequestHeader(value = "X-Sharer-User-Id") Long idRequestor) {
        return itemRequestService.findAllRequests(idRequestor);
    }

    @GetMapping("/all")
    List<ItemRequestDto> findAllForeignRequests(@RequestHeader(value = "X-Sharer-User-Id") Long idUser,
                                                @RequestParam(name = "from", defaultValue = "0")
                                                @Positive Integer index,
                                                @RequestParam(name = "size", defaultValue = "10")
                                                @Positive Integer size) {

        return itemRequestService.findAllForeignRequests(idUser, PageRequest.of(index, size,
                Sort.by("created").descending()));
    }

    @GetMapping("/{requestId}")
    ItemRequestDto findRequestById(@RequestHeader(value = "X-Sharer-User-Id") Long idUser,
                                   @PathVariable(name = "requestId") Long requestId) {
        return itemRequestService.findRequestById(requestId, idUser);
    }
}
