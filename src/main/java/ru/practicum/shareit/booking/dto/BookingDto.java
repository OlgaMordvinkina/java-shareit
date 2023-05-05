package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import ru.practicum.shareit.validators.ValidDateConstraint;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@ValidDateConstraint
public class BookingDto {
    @NonNull
    private Long itemId;
    @NonNull
    @FutureOrPresent
    private LocalDateTime start;
    @NonNull
    @Future
    private LocalDateTime end;
}
