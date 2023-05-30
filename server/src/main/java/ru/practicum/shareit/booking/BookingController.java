package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.exceptions.NotValidDataException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.xml.bind.ValidationException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService service;

    @PostMapping()
    public BookingFullDto createBooking(@Valid @RequestBody BookingDto bookingDto,
                                        @RequestHeader("X-Sharer-User-Id") Long bookerId) throws NotValidDataException {
        log.info("Получен запрос POST /bookings");
        return service.createBooking(bookingDto, bookerId);
    }

    @PatchMapping(value = "/{bookingId}")
    public BookingFullDto setStatusBooking(@PathVariable Long bookingId,
                                           @RequestParam Boolean approved,
                                           @RequestHeader("X-Sharer-User-Id") Long userId) throws NotValidDataException {
        log.info("Получен запрос PATCH /bookings/{bookingId}");
        return service.setStatusBooking(bookingId, userId, approved);
    }

    @GetMapping(value = "/{bookingId}")
    public BookingFullDto getBooking(@PathVariable Long bookingId,
                                     @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос GET /bookings/{bookingId}");
        return service.getBooking(bookingId, userId);
    }

    @GetMapping()
    public List<BookingFullDto> getBookings(@RequestParam(defaultValue = "ALL") String state,
                                            @RequestHeader("X-Sharer-User-Id") Long bookerId,
                                            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                            @RequestParam(defaultValue = "10") @Positive int size) throws ValidationException {
        log.info("Получен запрос GET /bookings");
        return service.getBookings(state, bookerId, from, size);
    }

    @GetMapping(value = "/owner")
    public List<BookingFullDto> getBookingsOwner(@RequestParam(defaultValue = "ALL") String state,
                                                 @RequestHeader("X-Sharer-User-Id") Long bookerId,
                                                 @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                 @RequestParam(defaultValue = "10") @Positive int size) throws NotValidDataException {
        log.info("Получен запрос GET /bookings");
        return service.getBookingsOwner(state, bookerId, from, size);
    }
}
