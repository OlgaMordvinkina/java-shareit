package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.exceptions.NotValidDataException;

import javax.validation.Valid;
import javax.xml.bind.ValidationException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService service;

    @PostMapping()
    public BookingFullDto createBooking(@Valid @RequestBody BookingDto bookingDto,
                                        @RequestHeader("X-Sharer-User-Id") Long bookerId) throws NotValidDataException {
        return service.createBooking(bookingDto, bookerId);
    }

    @PatchMapping(value = "/{bookingId}")
    public BookingFullDto setStatusBooking(@PathVariable Long bookingId,
                                           @RequestParam Boolean approved,
                                           @RequestHeader("X-Sharer-User-Id") Long userId) throws NotValidDataException {
        return service.setStatusBooking(bookingId, userId, approved);
    }

    @GetMapping(value = "/{bookingId}")
    public BookingFullDto getBooking(@PathVariable Long bookingId,
                                     @RequestHeader("X-Sharer-User-Id") Long userId) {
        return service.getBooking(bookingId, userId);
    }

    @GetMapping()
    public List<BookingFullDto> getBookings(@RequestParam(defaultValue = "ALL") String state,
                                            @RequestHeader("X-Sharer-User-Id") Long bookerId) throws ValidationException {
        return service.getBookings(state, bookerId);
    }

    @GetMapping(value = "/owner")
    public List<BookingFullDto> getBookingsOwner(@RequestParam(defaultValue = "ALL") String state,
                                                 @RequestHeader("X-Sharer-User-Id") Long bookerId) throws NotValidDataException {
        return service.getBookingsOwner(state, bookerId);
    }

}
