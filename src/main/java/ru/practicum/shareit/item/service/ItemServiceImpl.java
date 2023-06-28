package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.LastBookingDto;
import ru.practicum.shareit.booking.dto.NextBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.error.exception.AccessErrorException;
import ru.practicum.shareit.error.exception.IncorrectDateError;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.mapper.CommentMapper;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.repository.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Override
    public ItemDto createItem(Item item) {
        if (!userRepository.existsById(item.getOwner())) {
            throw new NotFoundException("Пользователя с id = " + item.getOwner() + " не существует.");
        }
        if (item.getName() == null || item.getName().isEmpty()) {
            throw new IncorrectDateError("У вещи отсутствует название.");
        }
        if (item.getDescription() == null || item.getDescription().isEmpty()) {
            throw new IncorrectDateError("Отсутствует описание вещи.");
        }
        if (item.getAvailable() == null) {
            throw new IncorrectDateError("Не выставлен статус вещи.");
        }
        Item createdItem = itemRepository.save(item);
        return ItemMapper.toItemDto(createdItem);
    }

    @Override
    public ItemPatchDto updateItem(ItemPatchDto itemDto) {
        Item updatedItem = itemRepository.getById(itemDto.getId());
        if (itemDto.getId() == null || !itemDto.getId().equals(updatedItem.getId())) {
            throw new AccessErrorException("У вас нет прав для редактирования");
        }
        if (!userRepository.existsById(itemDto.getOwner())) {
            throw new NotFoundException("Пользователя с id = " + itemDto.getOwner() + " не существует.");
        }
        if (!itemRepository.existsById(itemDto.getId())) {
            throw new NotFoundException("Вещи с id = " + itemDto.getOwner() + " не существует.");
        }
        Item ans = ItemMapper.toUp(updatedItem, itemDto);
        itemRepository.up(ans.getName(), ans.getDescription(), ans.getAvailable(),
                ans.getId());
        return ItemMapper.toItemPatchDto(ans);
    }

    @Override
    public ItemOwnerDto findItemById(Long idOwner, Long id) {
        if (!itemRepository.existsById(id)) {
            throw new NotFoundException("Вещи с id " + id + " не существует.");
        }
        Item item = itemRepository.getById(id);
        List<CommentDto> commentsDto = commentRepository.findByItemId(item.getId()).stream()
                .map(CommentMapper::toCommentDto).collect(Collectors.toList());

        if (!idOwner.equals(item.getOwner())) {
            ItemOwnerDto ans = ItemMapper.toItemOwnerDto(item);
            ans.setComments(commentsDto);
            return ans;

        } else {
            ItemOwnerDto ans = ItemMapper.toItemOwnerDto(item);

            Optional<Booking> lastBooking = bookingRepository.findFirstByItemIdAndStatusAndStartBeforeOrderByStartDesc(
                    id, Status.APPROVED, LocalDateTime.now());

            ans.setLastBooking(lastBooking.isEmpty() ? null : LastBookingDto.builder()
                    .id(lastBooking.get().getId())
                    .bookerId(lastBooking.get().getBooker().getId())
                    .start(lastBooking.get().getStart())
                    .end(lastBooking.get().getEnd())
                    .build());
            Optional<Booking> nextBooking = bookingRepository.findFirstByItemIdAndStatusAndStartAfterOrderByStartAsc(
                    id, Status.APPROVED, LocalDateTime.now());

            ans.setNextBooking(nextBooking.isEmpty() ? null : NextBookingDto.builder()
                    .id(nextBooking.get().getId())
                    .bookerId(nextBooking.get().getBooker().getId())
                    .start(nextBooking.get().getStart())
                    .end(nextBooking.get().getEnd())
                    .build());
            ans.setComments(commentsDto);
            return ans;
        }
    }

    @Override
    public List<ItemOwnerDto> findItemsByIdOwner(Long idOwner) {
        List<Item> items = itemRepository.findByOwner(idOwner);
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
        return itemOwnerDtoList;
    }

    @Override
    public List<ItemDto> findItemsByText(String text) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }
        return itemRepository.findItemsByText(text.toLowerCase())
                .stream()
                .map(item -> ItemMapper.toItemDto(item)).collect(Collectors.toList());
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