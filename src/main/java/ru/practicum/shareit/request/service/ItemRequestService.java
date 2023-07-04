package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ItemRequestService {
    ItemRequestDto addRequest(ItemRequestDto requestDto, Long idRequestor, LocalDateTime time);

    List<ItemRequestDto> findAllRequests(Long idRequestor);

    List<ItemRequestDto> findAllForeignRequests(Long idUser, Long index, Long size);

    ItemRequestDto findRequestById(Long idRequest);

}
