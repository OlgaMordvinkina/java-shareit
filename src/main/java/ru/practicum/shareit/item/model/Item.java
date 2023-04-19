package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.user.model.User;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Item {
    private Long id;
    private String name;
    private String description;
    private User owner;
    private Boolean available;
}