package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.error.exception.IncorrectDateError;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDtoForRequestEntity;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestDto addRequest(ItemRequestDto requestDto, Long idRequestor, LocalDateTime time) {
        if (requestDto.getDescription() == null || requestDto.getDescription().isEmpty()) {
            throw new IncorrectDateError("Описание не может быть не заполненным");
        }

        User requester = userRepository.findById(idRequestor)
                .orElseThrow(() -> new NotFoundException("Не верные данные по id пользователя."));

        ItemRequest addedRequest = ItemRequestMapper.toItemRequest(requestDto, requester);

        addedRequest.setCreated(time);

        addedRequest = requestRepository.save(addedRequest);
        return ItemRequestMapper.toItemRequestDto(addedRequest);
    }

    @Override
    public List<ItemRequestDto> findAllRequests(Long idRequestor) {
        List<ItemRequest> itemRequests = new ArrayList<>();//заготовка для ответа
        List<Long> idsItems = new ArrayList<>();//заготовка для id Item
        List<Item> items = new ArrayList<>();//заготовка для Item, которые пойдут в requestDto, для мапинга
        List<ItemDtoForRequestEntity> itemsForAns = new ArrayList<>();//items которые уйдут на ответ

        if (!userRepository.existsById(idRequestor)) {
            throw new NotFoundException("Не верные данные по id пользователя.");
        }

        itemRequests.addAll(requestRepository.findByRequestorIdOrderByCreatedDesc(idRequestor));
        List<ItemRequestDto> ans = itemRequests.stream()
                .map(ItemRequestMapper::toItemRequestDto).collect(Collectors.toList());

        itemRequests.stream()
                .map(request -> idsItems.add(request.getId()));
        items.addAll(itemRepository.findByRequestId(idsItems));
        itemsForAns.addAll(items.stream().map(ItemMapper::toItemDtoForRequest).collect(Collectors.toList()));

        for (ItemDtoForRequestEntity item : itemsForAns) {
            for (ItemRequestDto request : ans) {
                if (item.getRequest().getId().equals(request.getId())) {
                    request.getItems().add(item);
                    break;
                }
            }
        }
        return ans;
    }

    @Override
    public List<ItemRequestDto> findAllForeignRequests(Long idUser, Long index, Long size) {
        if (!userRepository.existsById(idUser)) {
            throw new NotFoundException("Не верные данные по id пользователя.");
        }

        return null;
    }

    @Override
    public ItemRequestDto findRequestById(Long idRequest) {
        return null;
    }
}
