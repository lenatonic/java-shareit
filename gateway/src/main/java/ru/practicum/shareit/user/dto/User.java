package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Setter
@AllArgsConstructor
public class User {

    private Long id;

    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    @NotBlank(message = "Поле email не может быть пустым")
    @Email(message = "Введено некорректное значение email")
    private String email;
}