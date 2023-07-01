package ru.practicum.shareit.booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingEnterDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDto createBooking(@RequestHeader(value = "X-Sharer-User-Id") Long idBooker,
                                    @RequestBody BookingEnterDto bookingEnterDto) {
        return bookingService.createBooking(idBooker, bookingEnterDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto changeApproved(@RequestHeader(value = "X-Sharer-User-Id") Long idOwner,
                                     @PathVariable("bookingId") Long bookingId,
                                     @RequestParam("approved") Boolean approved) {
        return bookingService.approvedBooking(bookingId, idOwner, approved);
    }

    @GetMapping("/owner")
    public List<BookingDto> findAllBookingsByIdOwner(@RequestHeader(value = "X-Sharer-User-Id") Long idOwner,
                                                     @RequestParam(value = "state", defaultValue = "ALL")
                                                     String state) {
        return bookingService.findAllBookingsByIdOwner(idOwner, state);
    }

    @GetMapping
    public List<BookingDto> findAllBookingsByIdUser(@RequestHeader(value = "X-Sharer-User-Id") Long idUser,
                                                    @RequestParam(value = "state", defaultValue = "ALL")
                                                    String state) {
        return bookingService.findAllBookingsByIdUser(idUser, state);
    }

    @GetMapping("/{bookingId}")
    public BookingDto findBookingById(@RequestHeader(value = "X-Sharer-User-Id") Long idUser,
                                      @PathVariable Long bookingId) {
        return bookingService.findBookingById(idUser, bookingId);
    }
}