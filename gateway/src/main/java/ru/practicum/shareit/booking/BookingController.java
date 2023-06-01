package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient client;

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @RequestBody @Valid BookItemRequestDto requestDto) {
        log.info("Creating booking {}, userId={}", requestDto, userId);
        return client.createBooking(userId, requestDto);
    }

    @PatchMapping(value = "/{bookingId}")
    public ResponseEntity<Object> setStatusBooking(@PathVariable Long bookingId,
                                                   @RequestParam Boolean approved,
                                                   @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос PATCH /bookings/{bookingId}");
        return client.setStatusBooking(userId, bookingId, approved);
    }

    @GetMapping
    public ResponseEntity<Object> getBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestParam(name = "state", defaultValue = "all") String state,
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingState states = BookingState.from(state)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + state));
        log.info("Get booking with state {}, userId={}, from={}, size={}", state, userId, from, size);
        return client.getBookings(userId, states, from, size);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return client.getBooking(userId, bookingId);
    }

    @GetMapping(value = "/owner")
    public ResponseEntity<Object> getBookingsOwner(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                                                   @RequestParam(defaultValue = "ALL") String state,
                                                   @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                   @Positive @RequestParam(defaultValue = "10") int size) {
        BookingState states = BookingState.from(state)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + state));
        log.info("Получен запрос GET /bookings");
        return client.getBookingsOwner(bookerId, states, from, size);
    }
}
