package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.LastBookingDto;
import ru.practicum.shareit.booking.dto.NextBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.error.exception.AccessErrorException;
import ru.practicum.shareit.error.exception.IncorrectDateError;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.mapper.CommentMapper;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.repository.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository requestRepository;

    @Override
    @Transactional
    public ItemDto createItem(ItemDto item, Long idUser) {
        Item createdItem = ItemMapper.toItem(item);

        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new NotFoundException("Пользователя с id = " + idUser + " не существует."));

        ItemRequest itemRequest = null;
        if (item.getRequestId() != null) {
            itemRequest = requestRepository.findById(item.getRequestId()).orElseThrow(() ->
                    new IncorrectDateError("Запроса с id = " + item.getRequestId() + " не существует"));
        }

        createdItem.setOwner(user);
        createdItem.setRequest(itemRequest);
        return ItemMapper.toItemDto(itemRepository.save(createdItem));
    }

    @Override
    public ItemPatchDto updateItem(ItemPatchDto itemDto, Long idItem, Long idOwner) {
        itemDto.setId(idItem);
        itemDto.setOwner(idOwner);
        Item updatedItem = itemRepository.findById(itemDto.getId())
                .orElseThrow(() -> new NotFoundException("Вещи с id " + itemDto.getId() + " не существует."));
        if (!userRepository.existsById(itemDto.getOwner())) {
            throw new NotFoundException("Пользователя с id = " + itemDto.getOwner() + " не существует.");
        }
        if (itemDto.getId() == null || !idOwner.equals(updatedItem.getOwner().getId())) {
            throw new AccessErrorException("У вас нет прав для редактирования");
        }
        Item ans = ItemMapper.toUp(updatedItem, itemDto);
        itemRepository.save(ans);
        return ItemMapper.toItemPatchDto(ans);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemOwnerDto findItemById(Long idOwner, Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Вещи с id " + id + " не существует."));
        List<CommentDto> commentsDto = commentRepository.findByItemId(item.getId()).stream()
                .map(CommentMapper::toCommentDto).collect(Collectors.toList());

        if (!idOwner.equals(item.getOwner().getId())) {
            ItemOwnerDto ans = ItemMapper.toItemOwnerDto(item);
            ans.setComments(commentsDto);
            return ans;

        } else {
            ItemOwnerDto ans = ItemMapper.toItemOwnerDto(item);

            Optional<Booking> lastBooking = bookingRepository.findFirstByItemIdAndStatusAndStartBeforeOrderByStartDesc(
                    id, Status.APPROVED, LocalDateTime.now());

            ans.setLastBooking(lastBooking.map(booking -> LastBookingDto.builder()
                    .id(booking.getId())
                    .bookerId(booking.getBooker().getId())
                    .start(booking.getStart())
                    .end(booking.getEnd())
                    .build()).orElse(null));
            Optional<Booking> nextBooking = bookingRepository.findFirstByItemIdAndStatusAndStartAfterOrderByStartAsc(
                    id, Status.APPROVED, LocalDateTime.now());

            ans.setNextBooking(nextBooking.map(booking -> NextBookingDto.builder()
                    .id(booking.getId())
                    .bookerId(booking.getBooker().getId())
                    .start(booking.getStart())
                    .end(booking.getEnd())
                    .build()).orElse(null));
            ans.setComments(commentsDto);
            return ans;
        }
    }

    @Override
    public List<ItemOwnerDto> findItemsByIdOwner(Long idOwner, Pageable pageable) {
        User owner = userRepository.findById(idOwner)
                .orElseThrow(() -> new NotFoundException("Пользователя с id = " + idOwner + " не существует."));
        List<Item> items = itemRepository.findByOwner(owner, pageable).getContent();
        if (items.isEmpty()) {
            return new ArrayList<>();
        }

        List<ItemOwnerDto> itemOwnerDtoList = items.stream()
                .map(ItemMapper::toItemOwnerDto)
                .collect(Collectors.toList());

        for (ItemOwnerDto itemOwnerDto : itemOwnerDtoList) {

            List<CommentDto> commentsDto = commentRepository.findByItemId(itemOwnerDto.getId()).stream()
                    .map(CommentMapper::toCommentDto).collect(Collectors.toList());
            itemOwnerDto.setComments(commentsDto);

            Optional<Booking> lastBooking = bookingRepository.findFirstByItemIdAndStatusAndStartBeforeOrderByStartDesc(
                    itemOwnerDto.getId(), Status.APPROVED, LocalDateTime.now());

            itemOwnerDto.setLastBooking(lastBooking.isEmpty() ? LastBookingDto.builder().build() : LastBookingDto.builder()
                    .id(lastBooking.get().getId())
                    .bookerId(lastBooking.get().getBooker().getId())
                    .start(lastBooking.get().getStart())
                    .end(lastBooking.get().getEnd())
                    .build());

            Optional<Booking> nextBooking = bookingRepository.findFirstByItemIdAndStatusAndStartAfterOrderByStartAsc(
                    itemOwnerDto.getId(), Status.APPROVED, LocalDateTime.now());

            itemOwnerDto.setNextBooking(nextBooking.isEmpty() ? NextBookingDto.builder().build() : NextBookingDto.builder()
                    .id(nextBooking.get().getId())
                    .bookerId(nextBooking.get().getBooker().getId())
                    .start(nextBooking.get().getStart())
                    .end(nextBooking.get().getEnd())
                    .build());
        }

        for (ItemOwnerDto itemOwnerDto : itemOwnerDtoList) {
            if (itemOwnerDto.getLastBooking().getBookerId() == null) {
                itemOwnerDto.setLastBooking(null);
            }
            if (itemOwnerDto.getNextBooking().getBookerId() == null) {
                itemOwnerDto.setNextBooking(null);
            }
        }
        return itemOwnerDtoList.stream().sorted(Comparator.comparingLong(ItemOwnerDto::getId)).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> findItemsByText(String text, Pageable pageable) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }
        return itemRepository.findItemsByText(text.toLowerCase(), pageable)
                .stream()
                .map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(Long userId, Long itemId, CommentDto commentDto) {
        if (commentDto.getText() == null || commentDto.getText().isEmpty()) {
            throw new IncorrectDateError("Комментарий не должен быть пустым");
        }
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new NotFoundException("Вещи с id = " + itemId + " не существует."));
        User author = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователя с id = " + itemId + " не существует."));
        if (bookingRepository.findTop1BookingByItemIdAndBookerIdAndEndIsBeforeAndStatusOrderByStartDesc(
                itemId, userId, LocalDateTime.now(), Status.APPROVED) == null) {
            throw new IncorrectDateError("Вы не бронировали эту вещь.");
        } else {
            Comment comment = CommentMapper.toComment(commentDto, LocalDateTime.now());
            comment.setItem(item);
            comment.setAuthor(author);

            comment = commentRepository.save(comment);
            return CommentMapper.toCommentDto(comment);
        }
    }
}