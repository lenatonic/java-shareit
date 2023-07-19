package ru.practicum.shareit.user.dto;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
public class UserDto {
    Long id;

    @NotBlank
    String name;

    @Email
    String email;
}