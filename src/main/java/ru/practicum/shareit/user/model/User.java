package ru.practicum.shareit.user.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long id;

    @NotBlank(message = "Имя не может быть пустым")
    @Column(name = "name_user", nullable = false)
    private String name;

    @NotBlank(message = "Поле email не может быть пустым")
    @Email(message = "Введено некорректное значение email")
    @Column(name = "email_user", unique = true, nullable = false)
    private String email;
}