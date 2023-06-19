package ru.practicum.shareit.user.dto;

import lombok.Data;

@Data
public class UserPatchDto {
    Long id;
    String name;
    String email;
}
