package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.dto.ItemDtoForRequestEntity;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Setter
@Getter
@Builder
public class ItemRequestDto {

    @Positive
    private Long id;

    private String description;

    private User requestor;

    private LocalDateTime created;

    private List<ItemDtoForRequestEntity> items;
}
