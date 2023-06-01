package ru.practicum.shareit.exceptions.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ErrorsDescription {
    private String fieldName;
    private String message;
}
