package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
@Validated
public class ItemRequestController {
    private final ItemRequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> addRequest(@Positive @RequestHeader(value = "X-Sharer-User-Id") Long idRequestor,
                                             @RequestBody @Valid ItemRequestDto requestDto) {
        return requestClient.addRequest(requestDto, idRequestor);
    }

    @GetMapping
    public ResponseEntity<Object> findAllRequests(@Positive @RequestHeader(value = "X-Sharer-User-Id") Long idRequestor) {
        return requestClient.findAllRequests(idRequestor);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAllForeignRequests(@Positive @RequestHeader(value = "X-Sharer-User-Id") Long idUser,
                                                         @RequestParam(name = "from", defaultValue = "0")
                                                         @Positive Integer from,
                                                         @RequestParam(name = "size", defaultValue = "10")
                                                         @Positive Integer size) {
        return requestClient.findAllForeignRequests(idUser, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findRequestById(@RequestHeader(value = "X-Sharer-User-Id") @Positive Long idUser,
                                                  @PathVariable(name = "requestId") @Positive Long requestId) {
        return requestClient.findRequestById(requestId, idUser);
    }
}
