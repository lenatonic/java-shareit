package ru.practicum.shareit.item.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */

@Getter
@Setter
@Entity
@Table(name = "items")
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Item {
    @Id
    @Column(name = "id_item")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Имя не может быть пустым")
    @Column(name = "name_item")
    private String name;

    @NotBlank(message = "Описание должно быть заполнено")
    private String description;

    @NotNull(message = "Поле доступности предмета не проставлено")
    private Boolean available;

    private Long owner;

    private Long requestId;
}