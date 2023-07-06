package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingEnterDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.error.exception.IncorrectDateError;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.error.exception.StatusErrorException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public BookingDto createBooking(Long id, BookingEnterDto bookingEnterDto) {
        bookingEnterDto.setBookerId(id);
        bookingEnterDto.setStatus(Status.WAITING);

        Item item = itemRepository.findById(bookingEnterDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещь с id " + bookingEnterDto.getItemId() + " не найдена."));

        User booker = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("У вас не достаточно прав."));

        if (id.equals(item.getOwner().getId())) {
            throw new NotFoundException("Владелец вещи не может подавать заявки на её бронь.");
        }

        if (!item.getAvailable()) {
            throw new IncorrectDateError("Вещь не доступна для брони");
        }

        if (bookingEnterDto.getEnd() == null || bookingEnterDto.getStart() == null ||
                bookingEnterDto.getStart().isBefore(LocalDateTime.now()) ||
                bookingEnterDto.getEnd().isBefore(bookingEnterDto.getStart()) ||
                bookingEnterDto.getEnd().isEqual(bookingEnterDto.getStart())) {
            throw new IncorrectDateError("Время задано не корректно.");
        }

        Booking savedBooker = bookingRepository.save(BookingMapper.toBookingForCreate(item, booker, bookingEnterDto));
        BookingDto ans = BookingMapper.toBookingDto(savedBooker);
        return ans;
    }

    @Override
    @Transactional
    public BookingDto approvedBooking(Long idBooking, Long idOwner, Boolean status) {
        User user = userRepository.findById(idOwner)
                .orElseThrow(() -> new NotFoundException("У вас не достаточно прав."));

        Booking booking = bookingRepository.findById(idBooking)
                .orElseThrow(() -> new NotFoundException("Бронь с id " + idBooking + " не найдена."));

        Item item = itemRepository.findById(booking.getItem().getId())
                .orElseThrow(() -> new NotFoundException("Вещь с id " + booking.getItem().getId() + " не найдена."));

        if (booking.getStatus().equals(Status.APPROVED)) {
            throw new IncorrectDateError("Бронь с id = " + booking.getId() + " уже подтверждена.");
        }

        if (item.getOwner().getId().equals(idOwner)) {
            if (status) {
                bookingRepository.approvedBooking(Status.APPROVED, idBooking);
                booking.setStatus(Status.APPROVED);
                return BookingMapper.toBookingDto(bookingRepository.getById(idBooking));
            } else {
                bookingRepository.approvedBooking(Status.REJECTED, idBooking);
                booking.setStatus(Status.REJECTED);
                return BookingMapper.toBookingDto(bookingRepository.getById(idBooking));
            }
        } else {
            throw new NotFoundException("У вас не достаточно прав.");
        }
    }

    @Override
    @Transactional
    public BookingDto findBookingById(Long idUser, Long idBooking) {
        userValidateExist(idUser);

        Booking booking = bookingRepository.findById(idBooking)
                .orElseThrow(() -> new NotFoundException("Брони с id: " + idBooking + " не существует."));

        Optional<Item> item = itemRepository.findById(booking.getItem().getId());
        if (booking.getBooker().getId().equals(idUser) || item.get().getOwner().getId().equals(idUser)) {
            return BookingMapper.toBookingDto(booking);
        } else {
            throw new NotFoundException("У вас не достаточно прав.");
        }
    }

    @Override
    @Transactional
    public List<BookingDto> findAllBookingsByIdUser(Long idUser, String stringState) {
        BookingState state = validationState(stringState);
        userValidateExist(idUser);

        List<Booking> bookings = new ArrayList<>();
        switch (state) {
            case ALL:
                bookings.addAll(bookingRepository.findByBookerIdOrderByStartDesc(idUser));
                break;
            case WAITING:
                bookings.addAll(bookingRepository.findByBookerIdAndStatusOrderByStartDesc(idUser, Status.WAITING));
                break;
            case REJECTED:
                bookings.addAll(bookingRepository.findByBookerIdAndStatusOrderByStartDesc(idUser, Status.REJECTED));
                break;
            case CURRENT:
                bookings.addAll(bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
                        idUser, LocalDateTime.now(), LocalDateTime.now()));
                break;
            case PAST:
                bookings.addAll(bookingRepository.findByBookerIdAndEndIsBeforeOrderByStartDesc(
                        idUser, LocalDateTime.now()));
                break;
            case FUTURE:
                bookings.addAll(bookingRepository.findByBookerIdAndStartIsAfterOrderByStartDesc(
                        idUser, LocalDateTime.now()));
                break;
        }
        return bookings.stream()
                .map(booking -> BookingMapper.toBookingDto(booking)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<BookingDto> findAllBookingsByIdOwner(Long idOwner, String stringState) {
        BookingState state = validationState(stringState);
        userValidateExist(idOwner);

        List<Booking> bookings = new ArrayList<>();
        switch (state) {
            case ALL:
                bookings.addAll(bookingRepository.findByItem_Owner_IdOrderByStartDesc(idOwner));//findByItemOwnerOrderByStartDesc(idOwner));
                break;
            case WAITING:
                bookings.addAll(bookingRepository.findByItem_Owner_IdAndStatusOrderByStartDesc(idOwner, Status.WAITING));//findByItemOwnerAndStatusOrderByStartDesc(idOwner, Status.WAITING));
                break;
            case REJECTED:
                bookings.addAll(bookingRepository.findByItem_Owner_IdAndStatusOrderByStartDesc(idOwner, Status.REJECTED));//findByItemOwnerAndStatusOrderByStartDesc(idOwner, Status.REJECTED));
                break;
            case CURRENT:
                bookings.addAll(bookingRepository.findAllByItemOwnerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(idOwner, LocalDateTime.now(), LocalDateTime.now()));
                break;
            case PAST:
                bookings.addAll(bookingRepository.findAllByItemOwnerAndEndIsBeforeOrderByStartDesc(idOwner, LocalDateTime.now()));
                break;
            case FUTURE:
                bookings.addAll(bookingRepository.findAllByItemOwnerAndStartIsAfterOrderByStartDesc(idOwner, LocalDateTime.now()));
                break;
        }
        return bookings.stream()
                .map(booking -> BookingMapper.toBookingDto(booking)).collect(Collectors.toList());
    }

    private BookingState validationState(String stringState) {
        BookingState state;
        try {
            state = BookingState.valueOf(stringState);
        } catch (RuntimeException e) {
            throw new StatusErrorException("Unknown state: " + stringState);
        }
        return state;
    }

    private boolean userValidateExist(Long idUser) {
        if (userRepository.existsById(idUser)) {
            return true;
        } else {
            throw new NotFoundException("Нет пользователя с id: " + idUser);
        }
    }
}