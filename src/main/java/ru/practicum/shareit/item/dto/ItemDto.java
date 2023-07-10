package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class ItemDto {
    Long id;

    @NotBlank
    @Size(max = 20)
    String name;

    @NotBlank
    @Size(max = 20)
    String description;

    @NotNull
    Boolean available;
    Long requestId;
}