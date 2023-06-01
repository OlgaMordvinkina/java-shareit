package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ItemRequestDto {
    private Long id;
    @NotBlank(message = "Не может быть пустое")
    private String description;
    private LocalDateTime created;
}
