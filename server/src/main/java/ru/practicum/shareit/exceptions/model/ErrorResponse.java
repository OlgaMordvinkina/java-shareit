package ru.practicum.shareit.exceptions.model;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ErrorResponse {
    private Integer code;
    private List<ErrorsDescription> errors;
}

