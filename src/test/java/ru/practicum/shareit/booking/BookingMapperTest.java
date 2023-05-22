package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class BookingMapperTest {
    private final LocalDateTime now = LocalDateTime.parse(LocalDate.now() + "T09:55:05.000001");
    private final User user = new User(1L, "name", "email@mail.ru");
    private final Item item = new Item(1L, "name", "description", user, true, 1L);
    private final Booking booking = new Booking(1L, now, now, item, user, Status.WAITING);
    private final BookingDto bookingDto = new BookingDto(1L, now, now);
    private final BookingFullDto bookingFullDto = new BookingFullDto(1L, now, now, item, user, Status.WAITING);
    private final BookingShortDto bookingShortDto = new BookingShortDto(1L, 1L, now, now);

    @Test
    void toBookingFullDtoFromBooking_returnedBookingFullDto() {
        BookingFullDto expectedBookingFullDto = BookingMapper.toBookingFullDto(booking);

        assertEquals(bookingFullDto, expectedBookingFullDto);
    }

    @Test
    void toBookingFullDtoFromBookingAndItemAndUser_returned() {
        BookingFullDto expectedBookingFullDto = BookingMapper.toBookingFullDto(bookingDto);
        expectedBookingFullDto.setItem(item);
        expectedBookingFullDto.setBooker(user);

        assertEquals(bookingFullDto, expectedBookingFullDto);
    }

    @Test
    void toBookingFullDtoFromBookingDtoAndItemAndUser_returned() {
        BookingFullDto expectedBookingFullDto = BookingMapper.toBookingFullDto(booking, item, user);

        assertEquals(bookingFullDto, expectedBookingFullDto);
    }

    @Test
    void toBooking_returnedBooking() {
        Booking expectedBooking = BookingMapper.toBooking(bookingDto, item, user);
        expectedBooking.setId(1L);

        assertEquals(booking.toString(), expectedBooking.toString());
    }

    @Test
    void toBookingShortDto_returnedBookingShortDto() {
        BookingShortDto expectedBookingShortDto = BookingMapper.toBookingShortDto(booking);

        assertEquals(bookingShortDto, expectedBookingShortDto);
    }
}
