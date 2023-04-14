package ru.practicum.shareit.item.model;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Item {
    private Long id;
    @NotBlank
    private String name;
    private String description;
    @NotBlank
    private Long owner;
    @NotBlank
    private Boolean available;
}