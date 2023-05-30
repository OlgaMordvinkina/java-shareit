package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.validators.ValidDateConstraint;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@ValidDateConstraint
public class BookingDto {
    private Long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
}
