package ru.practicum.shareit.user.model;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class User {
    private Long id;
    private String name;
    private String email;
}
