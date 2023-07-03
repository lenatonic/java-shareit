package ru.practicum.shareit.user.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserPatchDto {
    Long id;
    String name;
    String email;
}
