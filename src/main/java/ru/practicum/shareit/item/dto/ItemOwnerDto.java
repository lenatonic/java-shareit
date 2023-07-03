package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.LastBookingDto;
import ru.practicum.shareit.booking.dto.NextBookingDto;
import ru.practicum.shareit.item.comment.dto.CommentDto;

import java.util.List;

@Setter
@Getter
@Builder
public class ItemOwnerDto {
    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private Long owner;

    private Long request;

    private LastBookingDto lastBooking;

    private NextBookingDto nextBooking;

    private List<CommentDto> comments;
}