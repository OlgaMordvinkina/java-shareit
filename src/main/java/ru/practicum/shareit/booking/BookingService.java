package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.exceptions.NotValidDataException;

import javax.xml.bind.ValidationException;
import java.util.List;

public interface BookingService {

    /**
     * create a Booking
     *
     * @param bookingDto
     * @param bookerId
     * @return
     * @throws NotValidDataException
     */
    BookingFullDto createBooking(BookingDto bookingDto, Long bookerId) throws NotValidDataException;

    /**
     * set status Booking
     *
     * @param bookingId
     * @param bookerId
     * @param approved
     * @return
     * @throws NotValidDataException
     */
    BookingFullDto setStatusBooking(Long bookingId, Long bookerId, Boolean approved) throws NotValidDataException;

    /**
     * get a Booking by BookingId
     *
     * @param bookingId
     * @param bookerId
     * @return
     */
    BookingFullDto getBooking(Long bookingId, Long bookerId);


    /**
     * get a list Bookings by bookerId and state
     *
     * @param state
     * @param bookerId
     * @return
     * @throws ValidationException
     */
    List<BookingFullDto> getBookings(String state, Long bookerId) throws ValidationException;

    /**
     * get  a list Bookings owners
     *
     * @param state
     * @param bookerId
     * @return
     * @throws NotValidDataException
     */
    List<BookingFullDto> getBookingsOwner(String state, Long bookerId) throws NotValidDataException;
}
